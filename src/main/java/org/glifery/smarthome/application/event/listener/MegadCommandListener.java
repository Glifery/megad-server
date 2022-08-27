package org.glifery.smarthome.application.event.listener;

import lombok.RequiredArgsConstructor;
import org.glifery.smarthome.application.service.MegadService;
import org.glifery.smarthome.domain.model.event.PortStateActionChangeEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * This handler sends request to MegaD controller
 */
@Component
@RequiredArgsConstructor
public class MegadCommandListener extends AbstractListener {
    private final MegadService megadService;

    @EventListener
    public void handleActionEvent(PortStateActionChangeEvent portStateChangeActionEvent) {
        handleLog(portStateChangeActionEvent);

        megadService.sendCommand(portStateChangeActionEvent.getSingleAction());
    }
}
