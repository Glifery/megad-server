package org.glifery.smarthome.domain.event.aggregate;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.glifery.smarthome.application.exception.InvalidActionException;
import org.glifery.smarthome.domain.event.BaseAggregate;
import org.glifery.smarthome.domain.model.event.AbstractEvent;
import org.glifery.smarthome.domain.model.event.ActionEvent;
import org.glifery.smarthome.domain.model.event.BaseSingleActionAwareEvent;
import org.glifery.smarthome.domain.model.event.InitialPortStateEvent;
import org.glifery.smarthome.domain.model.megad.Port;
import org.glifery.smarthome.domain.model.megad.PortState;
import org.glifery.smarthome.domain.model.megad.SingleAction;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class PortStatesAggregate extends BaseAggregate {
    private final List<Port> ports;
    @Getter
    private final Map<Port, PortState> currentStates;

    public PortStatesAggregate(List<Port> ports) {
        this.ports = ports;
        this.currentStates = ports.stream()
                .collect(Collectors.toMap(port -> port, port -> PortState.create(port, PortState.State.OFF)));
    }

    @Override
    protected boolean supports(AbstractEvent event) {
        if (!(event instanceof ActionEvent) && !(event instanceof InitialPortStateEvent)) {
            return false;
        }

        return true;
    }

    @Override
    protected void on(AbstractEvent event) {
        Port port = ((BaseSingleActionAwareEvent)event).getSingleAction().getPort();

        currentStates
                .get(port)
                .setState(getNewState(((BaseSingleActionAwareEvent) event).getSingleAction()));
    }

    private PortState.State getNewState(SingleAction singleAction) {
        switch (singleAction.getAction()) {
            //SingleAction.OFF
            case OFF: return PortState.State.OFF;
            //SingleAction.ON
            case ON: return PortState.State.ON;
            //SingleAction.SWITCH
            case SWITCH: return currentStates.get(singleAction.getPort()).getState().equals(PortState.State.OFF)
                    ? PortState.State.ON
                    : PortState.State.OFF;
            default: throw new InvalidActionException(String.format("Action %s is not supported", singleAction.getAction()));
        }
    }
}
