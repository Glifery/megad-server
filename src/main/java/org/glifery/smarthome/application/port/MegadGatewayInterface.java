package org.glifery.smarthome.application.port;

import org.glifery.smarthome.domain.model.megad.MegaD;
import org.glifery.smarthome.domain.model.megad.ActionsList;
import org.glifery.smarthome.domain.model.megad.PortState;

import java.io.IOException;
import java.util.List;

public interface MegadGatewayInterface {
    String sendCommand(MegaD megaD, ActionsList actionsList) throws IOException;
    List<PortState> getAllStates(MegaD megaD) throws IOException;
}
