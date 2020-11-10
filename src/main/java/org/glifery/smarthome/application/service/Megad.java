package org.glifery.smarthome.application.service;

import lombok.RequiredArgsConstructor;
import org.glifery.smarthome.application.port.MegadGatewayInterface;
import org.glifery.smarthome.domain.model.megad.MegadId;
import org.glifery.smarthome.domain.model.megad.Operation;
import org.glifery.smarthome.domain.model.megad.PortAction;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class Megad {
    private final MegadGatewayInterface megadGateway;

    public void sendCommand(Operation operation) {
        Map<String, Operation> operations = splitByMegadId(operation);

        operations.entrySet().stream().forEach(op -> {
            try {
                megadGateway.sendCommand(new MegadId(op.getKey()), op.getValue());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private Map<String, Operation> splitByMegadId(Operation operation) {
        Map<String, Operation> operations = new HashMap<>();

        for (PortAction portAction : operation.getPortActions()) {
            String megadId = portAction.getPort().getMegadId().toString();

            if (Objects.isNull(operations.get(megadId))) {
                operations.put(megadId, new Operation());
            }

            operations.get(megadId).getPortActions().add(portAction);
        }

        return operations;
    }
}
