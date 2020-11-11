package org.glifery.smarthome.application.port;

import org.glifery.smarthome.domain.model.megad.ActionsList;
import org.glifery.smarthome.domain.model.megad.Port;
import org.glifery.smarthome.domain.model.megad.PortActionsList;

import java.util.List;

public interface PortActionsRepositoryInterface {
    void store(List<PortActionsList> portActionsLists);
    ActionsList getActionsList(Port port);
}
