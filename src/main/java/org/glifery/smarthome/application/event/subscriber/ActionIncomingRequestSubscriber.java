package org.glifery.smarthome.application.event.subscriber;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.glifery.smarthome.adapter.memory.PortActionsRepository;
import org.glifery.smarthome.application.service.Megad;
import org.glifery.smarthome.domain.event.ActionIncomingRequestEvent;
import org.glifery.smarthome.domain.model.megad.ActionsList;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ActionIncomingRequestSubscriber {
    private final PortActionsRepository portActionsRepository;
    private final Megad megad;

    @EventListener
    public void handleActionIncomingRequestEvent(ActionIncomingRequestEvent event) {
        log.warn(String.format("Got event %s", event.getRequest()));

        ActionsList actionsList = portActionsRepository.getActionsList(event.getRequest().getPort());

        log.warn(String.format("Run actions %s", actionsList.getSingleActions()));
        megad.sendCommand(actionsList);
    }
}
