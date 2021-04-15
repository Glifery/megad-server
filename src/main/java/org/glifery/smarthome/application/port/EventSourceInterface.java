package org.glifery.smarthome.application.port;

import org.glifery.smarthome.domain.model.event.AbstractEvent;

public interface EventSourceInterface {
    void publish(AbstractEvent event);

}
