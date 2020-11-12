package org.glifery.smarthome.domain.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.glifery.smarthome.domain.model.megad.ActionIncomingRequest;

@Getter
public class ActionIncomingRequestEvent extends AbstractEvent {
    private final ActionIncomingRequest request;

    public ActionIncomingRequestEvent(String name, ActionIncomingRequest request) {
        super(name);
        this.request = request;
    }
}
