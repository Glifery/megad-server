package org.glifery.smarthome.application.port;

import org.glifery.smarthome.domain.model.megad.MegadId;
import org.glifery.smarthome.domain.model.megad.Port;

public interface PortRepositoryInterface {
    Port findPort(MegadId megadId, Integer port);
}
