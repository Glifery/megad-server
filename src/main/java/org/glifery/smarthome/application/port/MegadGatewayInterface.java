package org.glifery.smarthome.application.port;

import org.glifery.smarthome.domain.model.megad.MegadId;
import org.glifery.smarthome.domain.model.megad.ActionsList;
import org.glifery.smarthome.domain.model.megad.PortState;

import java.io.IOException;
import java.util.List;

public interface MegadGatewayInterface {
    String sendCommand(MegadId megadId, ActionsList actionsList) throws IOException;
    List<PortState> getAllStates(MegadId megadId) throws IOException;
}
