package org.glifery.smarthome.domain.event.aggregate;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.glifery.smarthome.application.exception.InvalidActionException;
import org.glifery.smarthome.domain.event.BaseAggregate;
import org.glifery.smarthome.domain.model.event.AbstractEvent;
import org.glifery.smarthome.domain.model.event.ActionEvent;
import org.glifery.smarthome.domain.model.megad.Port;
import org.glifery.smarthome.domain.model.megad.PortState;
import org.glifery.smarthome.domain.model.megad.SingleAction;

@Slf4j
public class PortStateAggregate extends BaseAggregate {
    private final Port port;
    @Getter
    private final PortState currentState;

    public PortStateAggregate(Port port) {
        this.port = port;
        this.currentState = PortState.create(port, PortState.State.OFF);
    }

    @Override
    protected boolean supports(AbstractEvent event) {
        if (!(event instanceof ActionEvent)) {
            return false;
        }

        if (!((ActionEvent) event).getSingleAction().getPort().equals(port)) {
            return false;
        }

        return true;
    }

    @Override
    protected void on(AbstractEvent event) {
        currentState.setState(
                getNewState(((ActionEvent) event).getSingleAction())
        );
    }

    private PortState.State getNewState(SingleAction singleAction) {
        switch (singleAction.getAction()) {
            //SingleAction.OFF
            case 0: return PortState.State.OFF;
            //SingleAction.ON
            case 1: return PortState.State.ON;
            //SingleAction.SWITCH
            case 2: return currentState.getState().equals(PortState.State.OFF)
                    ? PortState.State.ON
                    : PortState.State.OFF;
            default: throw new InvalidActionException(String.format("Action %s is not supported", singleAction.getAction()));
        }
    }
}
