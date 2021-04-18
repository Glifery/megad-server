package org.glifery.smarthome.application.port;

import org.glifery.smarthome.domain.model.megad.MegaD;

public interface ControllerRepositoryInterface {
    MegaD findMegadId(String megadId);
}
