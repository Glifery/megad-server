package org.glifery.smarthome.application.port;

import org.glifery.smarthome.application.exception.InvalidPortException;
import org.glifery.smarthome.domain.model.megad.MegaD;
import org.glifery.smarthome.domain.model.megad.Port;

import java.util.List;

public interface PortRepositoryInterface {
    Port findPort(MegaD megaD, Integer port) throws InvalidPortException;
    List<Port> findAllPorts();
}
