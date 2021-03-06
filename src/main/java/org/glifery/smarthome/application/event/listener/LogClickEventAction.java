package org.glifery.smarthome.application.event.listener;

import lombok.extern.slf4j.Slf4j;
import org.glifery.smarthome.domain.event.ClickEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LogClickEventAction extends AbstractListener {
    @EventListener
    public void handleClickEvent(ClickEvent event) {
        log.warn(String.format("Handle %s event (%s)", event.getName(), event.getDateTime()));
    }
}
