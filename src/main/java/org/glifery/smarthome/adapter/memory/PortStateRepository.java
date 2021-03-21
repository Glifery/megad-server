package org.glifery.smarthome.adapter.memory;

import org.glifery.smarthome.application.port.PortStateRepositoryInterface;
import org.glifery.smarthome.domain.model.megad.Port;
import org.glifery.smarthome.domain.model.megad.PortState;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class PortStateRepository implements PortStateRepositoryInterface {
    private final Map<Port, PortState> portStateMap = new HashMap<>();

    @Override
    public PortState updatePortState(PortState portState) {
        PortState updatedPortState = portStateMap.entrySet().stream()
                .filter(portPortEntry -> portPortEntry.getKey().equals(portState.getPort()))
                .findFirst()
                .map(portStateEntry -> {
                    PortState portStateUpdated = portStateEntry.getValue();
                    portStateUpdated.setState(portState.getState());

                    return portStateUpdated;
                })
                .orElse(null);

        if (Objects.nonNull(updatedPortState)) {
            return updatedPortState;
        }

        portStateMap.put(portState.getPort(), portState);

        return portState;
    }

    @Override
    public PortState getPortState(Port port) {
        return portStateMap.entrySet().stream()
                .filter(entry -> entry.getKey().equals(port))
                .findFirst()
                .map(Map.Entry::getValue)
                .orElse(null);
    }
}
