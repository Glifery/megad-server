package org.glifery.smarthome.application.event.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.glifery.smarthome.application.service.MegadService;
import org.glifery.smarthome.domain.model.event.ActionEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * This handler sends request to MegaD controller
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MegadCommandListener extends AbstractListener {
    private final MegadService megadService;

    @EventListener
    public void handleActionEvent(ActionEvent actionEvent) {
        log.warn("----------------1111");
        megadService.sendCommand(actionEvent.getSingleAction());
    }
}
