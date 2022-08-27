package org.glifery.smarthome.domain.model.event;

import org.glifery.smarthome.domain.model.megad.SingleAction;

import java.time.LocalDateTime;

public class PortStateActionChangeEvent extends PortStateActionEvent {
    public PortStateActionChangeEvent(SingleAction singleAction, LocalDateTime dateTime) {
        super(singleAction, dateTime);
    }
}
