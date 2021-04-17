package org.glifery.smarthome.domain.model.event;

import lombok.Getter;
import org.glifery.smarthome.domain.model.megad.Port;

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

    private final Type type;
    private final Port port;

    public ClickEvent(Type type, Port port, LocalDateTime dateTime) {
        super(String.format("click.%s.%s", port, type), dateTime, ttl);
        this.type = type;
        this.port = port;
    }
}
