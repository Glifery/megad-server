package org.glifery.smarthome.application.port;

import org.glifery.smarthome.domain.model.event.AbstractEvent;

public interface EventStoreInterface {
    void publish(String publisherName, AbstractEvent abstractEvent);
}
