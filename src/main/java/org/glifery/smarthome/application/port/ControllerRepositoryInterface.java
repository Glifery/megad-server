package org.glifery.smarthome.application.port;

import org.glifery.smarthome.domain.model.megad.MegaD;

import java.util.List;

public interface ControllerRepositoryInterface {
    MegaD findMegaD(String megadId);
    List<MegaD> findAllMegaDs();
}
