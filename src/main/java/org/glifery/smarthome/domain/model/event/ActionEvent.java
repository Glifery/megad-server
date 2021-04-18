package org.glifery.smarthome.domain.model.event;

import org.glifery.smarthome.domain.model.megad.SingleAction;

import java.time.Duration;
import java.time.LocalDateTime;

public class ActionEvent extends BaseSingleActionAwareEvent {
    private static Duration ttl = Duration.ofDays(1);

    public ActionEvent(SingleAction singleAction, LocalDateTime dateTime) {
        super(String.format("action.%s", singleAction), singleAction, ttl, dateTime);
    }
}
