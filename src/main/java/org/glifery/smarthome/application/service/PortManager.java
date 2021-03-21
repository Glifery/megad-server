package org.glifery.smarthome.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.glifery.smarthome.application.exception.InvalidActionException;
import org.glifery.smarthome.application.port.PortStateRepositoryInterface;
import org.glifery.smarthome.domain.event.ActionEvent;
import org.glifery.smarthome.domain.model.megad.ActionsList;
import org.glifery.smarthome.domain.model.megad.PortState;
import org.glifery.smarthome.domain.model.megad.SingleAction;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class PortManager {
    private final PortStateRepositoryInterface portStateRepository;
    private final MegadService megadService;
    private final ApplicationEventPublisher publisher;

    public void applyActions(ActionsList actionsList, LocalDateTime dateTime) {
        log.info(String.format("Execute actions: %s", actionsList.getSingleActions()));
        megadService.sendCommand(actionsList);

        actionsList.getSingleActions().stream()
                .peek(this::updateStateAfterAction)
                .map(singleAction -> new ActionEvent(singleAction, dateTime))
                .forEach(publisher::publishEvent);
    }

    private void updateStateAfterAction(SingleAction singleAction) {
        PortState.State state;

        switch (singleAction.getAction()) {
            //SingleAction.OFF
            case 0: state = PortState.State.OFF; break;
            //SingleAction.ON
            case 1: state = PortState.State.ON; break;
            //SingleAction.SWITCH
            case 2: state = Optional
                    .ofNullable(portStateRepository.getPortState(singleAction.getPort()))
                    .map(portState -> portState.getState() == PortState.State.ON ? PortState.State.OFF : PortState.State.ON)
                    .orElse(PortState.State.ON); break;
            default: throw new InvalidActionException(String.format("Action %s is not implemented", singleAction.getAction()));
        }

        portStateRepository.updatePortState(
                PortState.create(singleAction.getPort(), state)
        );
    }
}
