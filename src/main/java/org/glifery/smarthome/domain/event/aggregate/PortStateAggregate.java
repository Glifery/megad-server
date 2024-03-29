package org.glifery.smarthome.domain.event.aggregate;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.glifery.smarthome.application.exception.InvalidActionException;
import org.glifery.smarthome.domain.event.BaseAggregate;
import org.glifery.smarthome.domain.model.event.*;
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
        if (!isPortStateRelated(event)) {
            return false;
        }

        if (!isCurrentPortRelated(event)) {
            return false;
        }

        return true;
    }

    @Override
    protected void on(AbstractEvent event) {
        currentState.setState(
                getNewState(((PortStateActionEvent) event).getSingleAction())
        );
    }

    private boolean isPortStateRelated(AbstractEvent event) {
        return ((event instanceof PortStateActionEvent) || (event instanceof InitialPortStateEvent));
    }

    private boolean isCurrentPortRelated(AbstractEvent event) {
        return ((BaseSingleActionAwareEvent) event).getSingleAction().getPort().equals(port);
    }

    private PortState.State getNewState(SingleAction singleAction) {
        switch (singleAction.getAction()) {
            //SingleAction.OFF
            case OFF: return PortState.State.OFF;
            //SingleAction.ON
            case ON: return PortState.State.ON;
            //SingleAction.SWITCH
            case SWITCH: return currentState.getState().equals(PortState.State.OFF)
                    ? PortState.State.ON
                    : PortState.State.OFF;
            default: throw new InvalidActionException(String.format("Action %s is not supported", singleAction.getAction()));
        }
    }
}
