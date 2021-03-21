package org.glifery.smarthome.application.event.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.glifery.smarthome.adapter.memory.PortActionsRepository;
import org.glifery.smarthome.application.service.PortManager;
import org.glifery.smarthome.domain.event.ClickEvent;
import org.glifery.smarthome.domain.model.megad.ActionsList;
import org.glifery.smarthome.domain.model.megad.Port;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
        List<String> eventNameParts = Arrays.stream(event.getName().split("\\.")).collect(Collectors.toList());
        Port port = Port.create(eventNameParts.get(0), Integer.parseInt(eventNameParts.get(1)));

        ActionsList predefinedActionsList = portActionsRepository.getActionsList(port);

        if (Objects.nonNull(predefinedActionsList)) {
            portManager.applyActions(predefinedActionsList, event.getDateTime());
        }
    }
}
