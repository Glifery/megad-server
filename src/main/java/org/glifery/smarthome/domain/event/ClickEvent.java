package org.glifery.smarthome.domain.event;

import java.time.LocalDateTime;

public class ClickEvent extends AbstractEvent {
    public enum Type {
        CLICK,
        CLICK_FIRST,
        CLICK_SINGLE,
        CLICK_DOUBLE,
        CLICK_HOLD
    }

    public ClickEvent(String name, LocalDateTime dateTime) {
        super(name);
        this.dateTime = dateTime;
    }
}
