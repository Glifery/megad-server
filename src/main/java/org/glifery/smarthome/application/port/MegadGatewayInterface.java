package org.glifery.smarthome.application.port;

import org.glifery.smarthome.domain.model.megad.MegadId;
import org.glifery.smarthome.domain.model.megad.Operation;

import java.io.IOException;

public interface MegadGatewayInterface {
    String sendCommand(MegadId megadId, Operation operation) throws IOException;
}
