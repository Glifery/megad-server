package org.glifery.smarthome.adapter.memory;

import org.glifery.smarthome.application.port.PortActionsRepositoryInterface;
import org.glifery.smarthome.domain.model.megad.ActionsList;
import org.glifery.smarthome.domain.model.megad.Port;
import org.glifery.smarthome.domain.model.megad.PortActionsList;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PortActionsRepository implements PortActionsRepositoryInterface {
    private Map<Port, ActionsList> actionsListMap = new HashMap<>();

    @Override
    public void store(List<PortActionsList> portActionsLists) {
        actionsListMap = new HashMap<>();

        portActionsLists.stream().forEach(portActionsList -> actionsListMap.put(portActionsList.getPort(),portActionsList.getActionsList()));
    }

    @Override
    public ActionsList getActionsList(Port port) {
        return actionsListMap.entrySet().stream().filter(
                entry -> entry.getKey().equals(port)
        ).findFirst().map(Map.Entry::getValue).orElse(null);
    }
}
