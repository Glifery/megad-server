package org.glifery.smarthome.domain.event;

import java.time.Duration;
import java.time.LocalDateTime;

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

    public ClickEvent(String name, LocalDateTime dateTime) {
        super(name, dateTime, ttl);
    }
}
