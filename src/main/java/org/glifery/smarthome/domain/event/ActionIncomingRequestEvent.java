package org.glifery.smarthome.domain.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.glifery.smarthome.domain.model.megad.ActionIncomingRequest;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
public class ActionIncomingRequestEvent extends AbstractEvent {
    private static Duration ttl = Duration.ofSeconds(10);

    private final ActionIncomingRequest request;

    public ActionIncomingRequestEvent(String name, ActionIncomingRequest request) {
        super(name, LocalDateTime.now(), ttl);
        this.request = request;
    }
}
