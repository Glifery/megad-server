package org.glifery.smarthome.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.glifery.smarthome.application.exception.InvalidActionException;
import org.glifery.smarthome.application.port.EventRepositoryInterface;
import org.glifery.smarthome.application.port.PortStateRepositoryInterface;
import org.glifery.smarthome.domain.event.ActionEvent;
import org.glifery.smarthome.domain.model.megad.ActionsList;
import org.glifery.smarthome.domain.model.megad.PortState;
import org.glifery.smarthome.domain.model.megad.SingleAction;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class PortManager {
    private final EventRepositoryInterface eventRepository;
    private final PortStateRepositoryInterface portStateRepository;

    public void applyActions(ActionsList actionsList, LocalDateTime dateTime) {
        log.info(String.format("Execute actions: %s", actionsList.getSingleActions()));

        actionsList.getSingleActions().stream()
                .map(this::normalizeSingleAction)
                .filter(Objects::nonNull)
                .peek(this::updateStateWithAction)
                .map(singleAction -> new ActionEvent(singleAction, dateTime))
                .forEach(eventRepository::publish);
    }

    private SingleAction normalizeSingleAction(SingleAction singleAction) {
        PortState currentState = Optional
                .ofNullable(portStateRepository.getPortState(singleAction.getPort()))
                .orElse(PortState.create(singleAction.getPort(), PortState.State.OFF));

        switch (singleAction.getAction()) {
            //SingleAction.OFF
            case 0: return currentState.getState().equals(PortState.State.OFF)
                    ? null
                    : singleAction;
            //SingleAction.ON
            case 1: return currentState.getState().equals(PortState.State.ON)
                    ? null
                    : singleAction;
            //SingleAction.SWITCH
            case 2: return currentState.getState().equals(PortState.State.OFF)
                    ? SingleAction.copyWithAction(singleAction, SingleAction.ON)
                    : SingleAction.copyWithAction(singleAction, SingleAction.OFF);
            default: throw new InvalidActionException(String.format("Action %s is not implemented", singleAction.getAction()));
        }
    }

    private void updateStateWithAction(SingleAction singleAction) {
        portStateRepository.updatePortState(PortState.create(singleAction.getPort(), getNewState(singleAction)));
    }

    private PortState.State getNewState(SingleAction singleAction) {
        switch (singleAction.getAction()) {
            //SingleAction.OFF
            case 0: return PortState.State.OFF;
            //SingleAction.ON
            case 1: return PortState.State.ON;
            default: throw new InvalidActionException(String.format("Action %s is not implemented", singleAction.getAction()));
        }
    }
}
