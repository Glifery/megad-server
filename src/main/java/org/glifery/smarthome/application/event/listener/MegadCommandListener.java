package org.glifery.smarthome.application.event.listener;

import lombok.RequiredArgsConstructor;
import org.glifery.smarthome.application.service.MegadService;
import org.glifery.smarthome.domain.model.event.PortStateActionChangeEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * This handler sends request to MegaD controller
 */
@Component
@RequiredArgsConstructor
public class MegadCommandListener extends AbstractListener {
    private final MegadService megadService;

    @Value("${application.listeners.MegadCommandListener.enabled}")
    private boolean enabled;

    @Override
    public String getListenerDescription() {
        return "This handler sends request to MegaD controller";
    }

    @Override
    public boolean getEnable() {
        return enabled;
    }

    @Override
    public void setEnable(boolean enable) {
        enabled = enable;
    }

    @EventListener
    public void handleActionEvent(PortStateActionChangeEvent portStateChangeActionEvent) {
        if (!getEnable()) {
            return;
        }

        handleLog(portStateChangeActionEvent);

        megadService.sendCommand(portStateChangeActionEvent.getSingleAction());
    }
}
