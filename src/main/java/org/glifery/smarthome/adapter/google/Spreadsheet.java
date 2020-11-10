package org.glifery.smarthome.adapter.google;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.BatchGetValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.glifery.smarthome.adapter.google.model.MegadRange;
import org.glifery.smarthome.application.configuration.MegadConfig;
import org.glifery.smarthome.application.configuration.SpreadsheetConfig;
import org.glifery.smarthome.domain.model.megad.MegadId;
import org.glifery.smarthome.domain.model.megad.Operation;
import org.glifery.smarthome.domain.model.megad.PortAction;
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
    private final Sheets sheets;

    public Spreadsheet(SpreadsheetConfig spreadsheetConfig, MegadConfig megadConfig) throws IOException, GeneralSecurityException {
        this.spreadsheetConfig = spreadsheetConfig;
        this.megadConfig = megadConfig;

        sheets = SheetsServiceUtil.getSheetsService(spreadsheetConfig);
        read();
    }

    public void read() throws IOException {
        List<MegadRange> megadRanges = megadConfig.getControllers().entrySet().stream().map(this::generateRangeForMegad).collect(Collectors.toList());

        List<String> ranges = megadRanges.stream().map(megadRange -> megadRange.getRange()).collect(Collectors.toList());

        BatchGetValuesResponse response = sheets.spreadsheets().values()
                .batchGet(spreadsheetConfig.getSpreadsheetId())
                .setRanges(ranges)
                .execute();

        List<ValueRange> valueRanges = response.getValueRanges();

        IntStream.range(0, valueRanges.size())
                .mapToObj(index -> generateOperationsForRange(megadRanges.get(index).getMegadId(), valueRanges.get(index)))
                .collect(Collectors.toList());
    }

    private MegadRange generateRangeForMegad(Map.Entry<String, MegadConfig.ControllerConfig> entry) {
        MegadId megadId = new MegadId(entry.getKey());

        String columnLetter = spreadsheetConfig.getMegadConfigColumn();
        String tabName = spreadsheetConfig.getSpreadsheetTab();

        Integer firstRow = entry.getValue().getSpreadsheet().getFirstRowForP0();
        Integer lastRow = firstRow + (MEGAD_ROWS_AMOUNT - 1);

        String range = String.format("%s!%s%s:%s%s", tabName, columnLetter, firstRow, columnLetter, lastRow);

        return new MegadRange(megadId, range);
    }

    private List<Operation> generateOperationsForRange(MegadId megadId, ValueRange valueRange) {
        return valueRange.getValues().get(0).stream().map(action -> generateOperation(megadId, action.toString())).collect(Collectors.toList());
    }

    private Operation generateOperation(MegadId megadId, String action) {
        return Operation.create(PortAction.create(megadId.toString(), 1, PortAction.SWITCH));
    }
}
