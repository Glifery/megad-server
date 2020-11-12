package org.glifery.smarthome.application.port;

import org.glifery.smarthome.domain.event.AbstractEvent;

public interface EventRepositoryInterface {
    void add(AbstractEvent abstractEvent);

    AbstractEvent findLatestByName(String name);
}
