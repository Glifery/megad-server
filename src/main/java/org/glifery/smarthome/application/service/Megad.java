package org.glifery.smarthome.application.service;

import lombok.RequiredArgsConstructor;
import org.glifery.smarthome.application.port.MegadGatewayInterface;
import org.glifery.smarthome.domain.model.megad.MegadId;
import org.glifery.smarthome.domain.model.megad.ActionsList;
import org.glifery.smarthome.domain.model.megad.SingleAction;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class Megad {
    private final MegadGatewayInterface megadGateway;

    public void sendCommand(ActionsList actionsList) {
        Map<String, ActionsList> operations = splitByMegadId(actionsList);

        operations.entrySet().stream().forEach(op -> {
            try {
                megadGateway.sendCommand(new MegadId(op.getKey()), op.getValue());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private Map<String, ActionsList> splitByMegadId(ActionsList actionsList) {
        Map<String, ActionsList> operations = new HashMap<>();

        for (SingleAction singleAction : actionsList.getSingleActions()) {
            String megadId = singleAction.getPort().getMegadId().toString();

            if (Objects.isNull(operations.get(megadId))) {
                operations.put(megadId, new ActionsList());
            }

            operations.get(megadId).getSingleActions().add(singleAction);
        }

        return operations;
    }
}
