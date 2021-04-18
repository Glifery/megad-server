package org.glifery.smarthome.domain.model.event;

import lombok.Getter;
import org.glifery.smarthome.domain.model.megad.SingleAction;

import java.time.Duration;
import java.time.LocalDateTime;

public class BaseSingleActionAwareEvent extends AbstractEvent {
    @Getter
    protected final SingleAction singleAction;

    public BaseSingleActionAwareEvent(String name, SingleAction singleAction, Duration ttl, LocalDateTime dateTime) {
        super(name, dateTime, ttl);
        this.singleAction = singleAction;
    }
}
