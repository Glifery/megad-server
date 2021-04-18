package org.glifery.smarthome.application.port;

import org.glifery.smarthome.domain.model.megad.MegadId;

public interface ControllerRepositoryInterface {
    MegadId findMegadId(String megadId);
}
