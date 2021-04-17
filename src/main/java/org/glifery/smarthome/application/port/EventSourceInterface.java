package org.glifery.smarthome.application.port;

import org.glifery.smarthome.domain.model.event.AbstractEvent;

import java.util.List;

public interface EventSourceInterface {
    void publish(AbstractEvent event);
    List<AbstractEvent> findAll();
    void clearByTtl();
}
