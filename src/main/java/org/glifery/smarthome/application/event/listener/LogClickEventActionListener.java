package org.glifery.smarthome.application.event.listener;

import lombok.extern.slf4j.Slf4j;
import org.glifery.smarthome.domain.model.event.ClickEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LogClickEventActionListener extends AbstractListener {
//    @EventListener
    public void handleClickEvent(ClickEvent event) {
        log.warn(String.format("Log %s event (%s)", event.getName(), event.getEventDateTime()));
    }
}
