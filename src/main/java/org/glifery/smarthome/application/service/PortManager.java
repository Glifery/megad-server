package org.glifery.smarthome.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.glifery.smarthome.application.exception.InvalidActionException;
import org.glifery.smarthome.application.port.EventStoreInterface;
import org.glifery.smarthome.application.port.EventSourceInterface;
import org.glifery.smarthome.domain.event.aggregate.PortStateAggregate;
import org.glifery.smarthome.domain.model.event.ActionEvent;
import org.glifery.smarthome.domain.model.megad.ActionsList;
import org.glifery.smarthome.domain.model.megad.PortState;
import org.glifery.smarthome.domain.model.megad.SingleAction;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class PortManager {
    private final EventStoreInterface eventRepository;
    private final EventSourceInterface eventSourceInterface;

    public void applyActions(ActionsList actionsList, LocalDateTime dateTime) {
        actionsList.getSingleActions().stream()
                .forEach(singleAction -> applyAction(singleAction, dateTime));
    }

    public void applyPortState(PortState portState) {
        SingleAction.Action action = SingleAction.Action.OFF;

        switch (portState.getState()) {
            case OFF: action = SingleAction.Action.OFF; break;
            case ON: action = SingleAction.Action.ON; break;
        }

        SingleAction singleAction = new SingleAction(portState.getPort(), action);
        applyAction(singleAction, LocalDateTime.now());
    }

    private void applyAction(SingleAction singleAction, LocalDateTime dateTime) {
        SingleAction normalizedSingleAction = normalizeSingleAction(singleAction);
        if (Objects.isNull(normalizedSingleAction)) {
            return;
        }

        log.info(String.format("Execute action: %s", normalizedSingleAction));

        ActionEvent actionEvent = new ActionEvent(normalizedSingleAction, dateTime);
        eventRepository.publish(actionEvent);
    }

    private SingleAction normalizeSingleAction(SingleAction singleAction) {
        PortStateAggregate aggregate = new PortStateAggregate(singleAction.getPort());
        aggregate.load(eventSourceInterface);

        PortState currentState = aggregate.getCurrentState();

        switch (singleAction.getAction()) {
            //SingleAction.OFF
            case OFF: return currentState.getState().equals(PortState.State.OFF)
                    ? null
                    : singleAction;
            //SingleAction.ON
            case ON: return currentState.getState().equals(PortState.State.ON)
                    ? null
                    : singleAction;
            //SingleAction.SWITCH
            case SWITCH: return currentState.getState().equals(PortState.State.OFF)
                    ? SingleAction.copyWithAction(singleAction, SingleAction.Action.ON)
                    : SingleAction.copyWithAction(singleAction, SingleAction.Action.OFF);
            default: throw new InvalidActionException(String.format("Action %s is not implemented", singleAction.getAction()));
        }
    }
}
