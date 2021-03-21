package org.glifery.smarthome.application.port;

import org.glifery.smarthome.domain.model.megad.Port;
import org.glifery.smarthome.domain.model.megad.PortState;

public interface PortStateRepositoryInterface {
    PortState updatePortState(PortState portState);
    PortState getPortState(Port port);
}
