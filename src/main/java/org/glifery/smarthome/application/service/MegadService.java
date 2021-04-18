package org.glifery.smarthome.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.glifery.smarthome.application.port.*;
import org.glifery.smarthome.domain.model.megad.ActionsList;
import org.glifery.smarthome.domain.model.megad.SingleAction;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

@Slf4j
@Component
@RequiredArgsConstructor
public class MegadService {
    private final ControllerRepositoryInterface controllerRepository;
    private final MegadGatewayInterface megadGateway;
    private final PortManager portManager;

    public void updateAllStates() {
        controllerRepository.findAllMegaDs().stream()
                .flatMap(megaD -> {
                    try {
                        return megadGateway.getAllStates(megaD).stream();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    return Stream.empty();
                })
                .forEach(portManager::applyPortState);

        log.info("All states have been refreshed");
    }

    public void sendCommand(ActionsList actionsList) {
        Map<String, ActionsList> operations = splitByMegadId(actionsList);

        operations.entrySet().stream().forEach(op -> {
            try {
                megadGateway.sendCommand(controllerRepository.findMegaD(op.getKey()), op.getValue());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void sendCommand(SingleAction singleAction) {
        sendCommand(ActionsList.create(singleAction));
    }

    private Map<String, ActionsList> splitByMegadId(ActionsList actionsList) {
        Map<String, ActionsList> operations = new HashMap<>();

        for (SingleAction singleAction : actionsList.getSingleActions()) {
            String megadId = singleAction.getPort().getMegaD().toString();

            if (Objects.isNull(operations.get(megadId))) {
                operations.put(megadId, new ActionsList());
            }

            operations.get(megadId).getSingleActions().add(singleAction);
        }

        return operations;
    }
}
