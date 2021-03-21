package org.glifery.smarthome.domain.event;

import org.glifery.smarthome.domain.model.megad.SingleAction;

import java.time.Duration;
import java.time.LocalDateTime;

public class ActionEvent extends AbstractEvent {
    private static Duration ttl = Duration.ofDays(1);

    private final SingleAction singleAction;

    public ActionEvent(SingleAction singleAction, LocalDateTime dateTime) {
        super(singleAction.toString(), dateTime, ttl);
        this.singleAction = singleAction;
    }
}
