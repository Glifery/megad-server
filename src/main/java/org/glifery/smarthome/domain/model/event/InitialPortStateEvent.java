package org.glifery.smarthome.domain.model.event;

import org.glifery.smarthome.domain.model.megad.SingleAction;

import java.time.Duration;
import java.time.LocalDateTime;

public class InitialPortStateEvent extends BaseSingleActionAwareEvent {
    private static Duration ttl = Duration.ofDays(1);

    public InitialPortStateEvent(SingleAction singleAction, LocalDateTime dateTime) {
        super(String.format("initial.%s", singleAction), singleAction, ttl, dateTime);
    }
}
