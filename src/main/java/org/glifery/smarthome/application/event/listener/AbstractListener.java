package org.glifery.smarthome.application.event.listener;

import lombok.extern.slf4j.Slf4j;
import org.glifery.smarthome.domain.event.AbstractEvent;

@Slf4j
public class AbstractListener {
    protected void handleLog(AbstractEvent event) {
        log.info(String.format("Handle %s event (%s)", event.getName(), event.getDateTime()));
    }

    protected void handleLog(AbstractEvent event, long delaySeconds) {
        log.info(String.format("Handle %s event (%s) with delay %ss", event.getName(), event.getDateTime(), delaySeconds));
    }
}
