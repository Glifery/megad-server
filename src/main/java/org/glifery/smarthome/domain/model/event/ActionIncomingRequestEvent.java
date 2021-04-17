package org.glifery.smarthome.domain.model.event;

import lombok.Getter;
import org.glifery.smarthome.domain.model.megad.ActionIncomingRequest;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
public class ActionIncomingRequestEvent extends AbstractEvent {
    private static Duration ttl = Duration.ofSeconds(10);

    private final ActionIncomingRequest request;

    public ActionIncomingRequestEvent(ActionIncomingRequest request) {
        super(String.format("request.%s", request), LocalDateTime.now(), ttl);
        this.request = request;
    }
}
