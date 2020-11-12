package org.glifery.smarthome.domain.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.glifery.smarthome.domain.model.megad.ActionIncomingRequest;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ActionIncomingRequestEvent {
    private final LocalDateTime dateTime = LocalDateTime.now();
    private final ActionIncomingRequest request;
}
