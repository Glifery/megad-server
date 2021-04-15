package org.glifery.smarthome.application.port;

import org.glifery.smarthome.domain.model.event.AbstractEvent;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepositoryInterface {
    void publish(AbstractEvent abstractEvent);

    Optional<AbstractEvent> findLatestByName(String name);

    List<AbstractEvent> findByNameDesc(String name, Integer limit);

    List<AbstractEvent> findAllAsc(LocalDateTime startDate);

    void clearByTtl();
}
