package org.glifery.smarthome.adapter.google;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.BatchGetValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.glifery.smarthome.adapter.google.model.MegadRange;
import org.glifery.smarthome.application.configuration.MegadConfig;
import org.glifery.smarthome.application.configuration.SpreadsheetConfig;
import org.glifery.smarthome.application.port.ControllerRepositoryInterface;
import org.glifery.smarthome.application.port.PortActionsRepositoryInterface;
import org.glifery.smarthome.application.port.PortRepositoryInterface;
import org.glifery.smarthome.application.util.ActionsListConverter;
import org.glifery.smarthome.domain.model.megad.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class Spreadsheet {
    private static final Integer MEGAD_ROWS_AMOUNT = 28;

    private final SpreadsheetConfig spreadsheetConfig;
    private final MegadConfig megadConfig;
    private final ControllerRepositoryInterface controllerRepository;
    private final PortRepositoryInterface portRepository;
    private final PortActionsRepositoryInterface portActionsRepository;
    private final Sheets sheets;

    public Spreadsheet(
            SpreadsheetConfig spreadsheetConfig,
            MegadConfig megadConfig,
            ControllerRepositoryInterface controllerRepository,
            PortRepositoryInterface portRepository,
            PortActionsRepositoryInterface portActionsRepository
    ) throws IOException, GeneralSecurityException {
        this.spreadsheetConfig = spreadsheetConfig;
        this.megadConfig = megadConfig;
        this.controllerRepository = controllerRepository;
        this.portRepository = portRepository;
        this.portActionsRepository = portActionsRepository;

        this.sheets = SheetsServiceUtil.getSheetsService(spreadsheetConfig);

        if (!spreadsheetConfig.isDisabled()) {
            List<PortActionsList> portActionsLists = readPortActionsLists();
            portActionsRepository.store(portActionsLists);
        }
    }

    public List<PortActionsList> readPortActionsLists() throws IOException {
        // Collect table ranges for each MegaD. Example: {megad1: Подключение!I8:J35, megad2: Подключение!I42:J69}
        List<MegadRange> megadRanges = megadConfig.getControllers().entrySet().stream().map(this::generateRangeForMegad).collect(Collectors.toList());

        // Collect only table ranges. Example: {Подключение!I8:J35, Подключение!I42:J69}
        List<String> ranges = megadRanges.stream().map(megadRange -> megadRange.getRange()).collect(Collectors.toList());

        BatchGetValuesResponse response = sheets.spreadsheets().values()
                .batchGet(spreadsheetConfig.getSpreadsheetId())
                .setRanges(ranges)
                .execute();

        // For each MegaD create list of PortActionsList. Example: {megad1: [{port 0: action: 1:1}, {port 0: action: 1:1}], megad2: ...}
        List<List<PortActionsList>> portActionsListsForRanges = IntStream.range(0, response.getValueRanges().size())
                .mapToObj(index -> generateOperationsForRange(megadRanges.get(index).getMegadId(), response.getValueRanges().get(index)))
                .collect(Collectors.toList());

        // Flattern 'list of list' into 'list'
        List<PortActionsList> portActionsLists = portActionsListsForRanges.stream().flatMap(List::stream).collect(Collectors.toList());

        return portActionsLists;
    }

    private MegadRange generateRangeForMegad(Map.Entry<String, MegadConfig.ControllerConfig> entry) {
        MegadId megadId = controllerRepository.findMegadId(entry.getKey());

        String firstColumnLetter = spreadsheetConfig.getMegadPortColumn();
        String lastColumnLetter = spreadsheetConfig.getMegadActionColumn();
        String tabName = spreadsheetConfig.getSpreadsheetTab();

        Integer firstRow = entry.getValue().getSpreadsheet().getFirstRowForP0();
        Integer lastRow = firstRow + (MEGAD_ROWS_AMOUNT - 1);

        String range = String.format("%s!%s%s:%s%s", tabName, firstColumnLetter, firstRow, lastColumnLetter, lastRow);

        return new MegadRange(megadId, range);
    }

    private List<PortActionsList> generateOperationsForRange(MegadId megadId, ValueRange valueRange) {
        return valueRange.getValues().stream()
                .map(row -> {
                    if (row.size() == 2) {
                        return generateOperation(megadId, row.get(0).toString(), row.get(1).toString());
                    }

                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private PortActionsList generateOperation(MegadId megadId, String initialPortString, String actionString) {
        Port initialPort = portRepository.findPort(megadId, Integer.parseInt(initialPortString));
        ActionsList actionsList = ActionsListConverter.fromActionString(portRepository, megadId, actionString);

        return new PortActionsList(initialPort, actionsList);
    }
}
