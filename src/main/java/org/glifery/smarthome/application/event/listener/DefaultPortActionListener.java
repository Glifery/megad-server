package org.glifery.smarthome.application.event.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.glifery.smarthome.adapter.memory.PortActionsRepository;
import org.glifery.smarthome.application.service.MegadService;
import org.glifery.smarthome.domain.event.ClickEvent;
import org.glifery.smarthome.domain.model.megad.ActionsList;
import org.glifery.smarthome.domain.model.megad.Port;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultPortActionListener {
    private final PortActionsRepository portActionsRepository;
    private final MegadService megadService;

    @EventListener
    public void handleClickEvent(ClickEvent event) {
        if (!event.getName().contains(ClickEvent.Type.CLICK_FIRST.toString())) {
            return;
        }

        log.info(String.format("Handle %s event", event.getName()));
        List<String> eventNameParts = Arrays.stream(event.getName().split("\\.")).collect(Collectors.toList());

        ActionsList actionsList = portActionsRepository.getActionsList(Port.create(eventNameParts.get(0), Integer.parseInt(eventNameParts.get(1))));

        if (Objects.nonNull(actionsList)) {
            log.warn(String.format("Execute actions: %s", actionsList.getSingleActions()));
            megadService.sendCommand(actionsList);
        }
    }
}
