package org.glifery.smarthome.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.glifery.smarthome.application.exception.InvalidActionException;
import org.glifery.smarthome.application.port.EventSourceInterface;
import org.glifery.smarthome.application.port.EventStoreInterface;
import org.glifery.smarthome.domain.event.aggregate.PortStateAggregate;
import org.glifery.smarthome.domain.model.event.PortStateActionChangeEvent;
import org.glifery.smarthome.domain.model.event.PortStateActionEvent;
import org.glifery.smarthome.domain.model.megad.PortState;
import org.glifery.smarthome.domain.model.megad.SingleAction;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class PortManager {
    private final EventStoreInterface eventStore;
    private final EventSourceInterface eventSourceInterface;

    public void syncPortState(PortState portState) {
        SingleAction.Action action;

        switch (portState.getState()) {
            case ON: action = SingleAction.Action.ON; break;
            default: action = SingleAction.Action.OFF; break;
        }

        SingleAction singleAction = new SingleAction(portState.getPort(), action);
        SingleAction actualizedSingleAction = actualizeSingleAction(singleAction);
        if (Objects.isNull(actualizedSingleAction)) {
            return;
        }

        PortStateActionEvent portStateActionEvent = new PortStateActionEvent(actualizedSingleAction, LocalDateTime.now());
        eventStore.publish(String.format("%s::syncPortState", this.getClass().getSimpleName()), portStateActionEvent);
    }

    public void applyAction(String actionProducerName, SingleAction singleAction, LocalDateTime dateTime) {
        SingleAction actualizedSingleAction = actualizeSingleAction(singleAction);
        if (Objects.isNull(actualizedSingleAction)) {
            return;
        }

        PortStateActionChangeEvent portStateChangeActionEvent = new PortStateActionChangeEvent(actualizedSingleAction, dateTime);
        eventStore.publish(actionProducerName, portStateChangeActionEvent);
    }

    private SingleAction actualizeSingleAction(SingleAction singleAction) {
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
