package org.glifery.smarthome.application.port;

import org.glifery.smarthome.domain.event.AbstractEvent;

import java.util.List;

public interface EventRepositoryInterface {
    void add(AbstractEvent abstractEvent);

    AbstractEvent findLatestByName(String name);

    List<AbstractEvent> findByNameDesc(String name, Integer limit);

    void clearByTtl();
}
