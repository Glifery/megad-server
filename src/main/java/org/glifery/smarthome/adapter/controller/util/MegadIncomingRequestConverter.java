package org.glifery.smarthome.adapter.controller.util;

import org.glifery.smarthome.application.port.PortRepositoryInterface;
import org.glifery.smarthome.domain.model.megad.*;

import java.util.Objects;
import java.util.Optional;

public class MegadIncomingRequestConverter {
    public static IncomingRequest createFromServerRequest(PortRepositoryInterface config, MegaD megaD, Integer portNumber, ActionIncomingRequest.Type type, Integer clickCounter, ActionIncomingRequest.Mode mode, StatusIncomingRequest.Status status) {
        Port port = config.findPort(megaD, portNumber);

        if (Objects.nonNull(status)) {
            return new StatusIncomingRequest(port, status);
        }

        return new ActionIncomingRequest(
                port,
                Optional.ofNullable(mode).orElse(ActionIncomingRequest.Mode.PRESS),
                Optional.ofNullable(type).orElse(ActionIncomingRequest.Type.SINGLE)
        );
    }
}
