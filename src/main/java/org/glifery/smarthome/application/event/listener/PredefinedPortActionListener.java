package org.glifery.smarthome.application.event.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.glifery.smarthome.adapter.memory.PortActionsRepository;
import org.glifery.smarthome.application.service.PortManager;
import org.glifery.smarthome.domain.model.event.ClickEvent;
import org.glifery.smarthome.domain.model.megad.ActionsList;
import org.glifery.smarthome.domain.model.megad.Port;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * This handler runs predefined (default) MegaD behavior for given port
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PredefinedPortActionListener extends AbstractListener {
    private final PortActionsRepository portActionsRepository;
    private final PortManager portManager;

    @EventListener
    public void handleClickEvent(ClickEvent event) {
        if (!event.getName().contains(ClickEvent.Type.CLICK_FIRST.toString())) {
            return;
        }

        handleLog(event);

        Port port = event.getPort();

        ActionsList predefinedActionsList = portActionsRepository.getActionsList(port);

        if (Objects.nonNull(predefinedActionsList)) {
            portManager.applyActions(predefinedActionsList, event.getEventDateTime());
        }
    }
}
