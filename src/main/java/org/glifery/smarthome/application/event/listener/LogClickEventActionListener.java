package org.glifery.smarthome.application.event.listener;

import lombok.extern.slf4j.Slf4j;
import org.glifery.smarthome.domain.model.event.ClickEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LogClickEventActionListener extends AbstractListener {
    @Value("${application.listeners.LogClickEventActionListener.enabled}")
    private boolean enabled;

    @Override
    public String getListenerName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public boolean getEnable() {
        return enabled;
    }

    @Override
    public void setEnable(boolean enable) {
        enabled = enable;
    }

    @Override
    public String getListenerDescription() {
        return "This handler just logs CLICK events with WARN log level. Disabled by default";
    }

    @EventListener
    public void handleClickEvent(ClickEvent event) {
        if (!getEnable()) {
            return;
        }

        log.warn(String.format("Log %s event (%s)", event.getName(), event.getEventDateTime()));
    }
}
