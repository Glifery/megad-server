package org.glifery.smarthome.application.event.listener;

import lombok.extern.slf4j.Slf4j;
import org.glifery.smarthome.domain.event.AbstractEvent;

import java.time.Duration;

@Slf4j
public class AbstractListener {
    protected void handleLog(AbstractEvent event) {
        log.debug(String.format("Handling event: %s (%s)", event.getName(), event.getDateTime()));
    }

    protected void handleLog(AbstractEvent event, Duration delay) {
        log.debug(String.format("Handling event: %s (%s) with delay %ss", event.getName(), event.getDateTime(), delay.getSeconds()));
    }
}
