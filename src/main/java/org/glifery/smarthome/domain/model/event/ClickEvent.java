package org.glifery.smarthome.domain.model.event;

import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
public class ClickEvent extends AbstractEvent {
    private static Duration ttl = Duration.ofDays(1);

    public enum Type {
        CLICK,
        CLICK_FIRST,
        CLICK_SINGLE,
        CLICK_DOUBLE,
        CLICK_TRIPLE,
        CLICK_HOLD,
        UNCLICK
    }

    private final ActionIncomingRequestEvent actionIncomingRequestEvent;

    public ClickEvent(String name, LocalDateTime dateTime, ActionIncomingRequestEvent actionIncomingRequestEvent) {
        super(name, dateTime, ttl);
        this.actionIncomingRequestEvent = actionIncomingRequestEvent;
    }
}
