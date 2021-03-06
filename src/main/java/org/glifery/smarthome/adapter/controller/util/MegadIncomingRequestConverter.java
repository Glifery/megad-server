package org.glifery.smarthome.adapter.controller.util;

import org.glifery.smarthome.domain.model.megad.*;

import java.util.Objects;
import java.util.Optional;

public class MegadIncomingRequestConverter {
    public static IncomingRequest createFromServerRequest(MegadId megadId, Integer portNumber, ActionIncomingRequest.ClickType clickType, Integer clickCounter, ActionIncomingRequest.Mode mode, StatusIncomingRequest.Status status) {
        Port port = new Port(megadId, portNumber);

        if (Objects.nonNull(status)) {
            return new StatusIncomingRequest(port, status);
        }

        return new ActionIncomingRequest(
                port,
                Optional.ofNullable(mode).orElse(ActionIncomingRequest.Mode.PRESS),
                Optional.ofNullable(clickType).orElse(ActionIncomingRequest.ClickType.SINGLE)
        );
    }
}
