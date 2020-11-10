package org.glifery.smarthome.domain.model.megad;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class Operation {
    @Getter
    private List<PortAction> portActions = new ArrayList<>();

    public static Operation create(PortAction... portActions) {
        Operation operation = new Operation();

        for (PortAction portAction : portActions) {
            operation.getPortActions().add(portAction);
        }

        return operation;
    }

    public String toString() {
        List<String> portActionsString = portActions.stream().map(PortAction::toString).collect(Collectors.toList());

        return String.join(";", portActionsString);
    }
}
