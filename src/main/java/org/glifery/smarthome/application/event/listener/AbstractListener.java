package org.glifery.smarthome.application.event.listener;

import lombok.extern.slf4j.Slf4j;
import org.glifery.smarthome.domain.model.event.AbstractEvent;

import java.time.Duration;

@Slf4j
public abstract class AbstractListener {
    public String getListenerName() {
        return this.getClass().getSimpleName();
    }

    public abstract String getListenerDescription();

    protected void handleLog(AbstractEvent event) {
        log.info(String.format(
                "%s: handling %s event: '%s' at (%s)",
                this.getListenerName(),
                event.getClass().getSimpleName(),
                event.getName(),
                event.getEventDateTime()
        ));
    }

    protected void handleLog(AbstractEvent event, Duration delay) {
        log.info(String.format(
                "%s: handling %s event: '%s' at (%s) with delay %ss",
                this.getListenerName(),
                event.getClass().getSimpleName(),
                event.getName(),
                event.getEventDateTime(),
                delay.getSeconds()
        ));
    }
}
