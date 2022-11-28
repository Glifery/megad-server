package org.glifery.smarthome.application.event.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.glifery.smarthome.adapter.memory.PortActionsRepository;
import org.glifery.smarthome.application.service.PortManager;
import org.glifery.smarthome.domain.model.event.ClickEvent;
import org.glifery.smarthome.domain.model.megad.ActionsList;
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

    @Override
    public String getListenerDescription() {
        return "This handler runs predefined (default) MegaD behavior for given port";
    }

    @EventListener
    public void handleClickEvent(ClickEvent event) {
        if (!event.getName().contains(ClickEvent.Type.CLICK_FIRST.toString())) {
            return;
        }

        handleLog(event);

        ActionsList predefinedActionsList = portActionsRepository.getActionsList(event.getPort());

        if (Objects.isNull(predefinedActionsList)) {
            return;
        }

        predefinedActionsList.getSingleActions().stream()
                .forEach(singleAction -> portManager.applyAction(this.getClass().getSimpleName(), singleAction, event.getEventDateTime()));
    }
}
