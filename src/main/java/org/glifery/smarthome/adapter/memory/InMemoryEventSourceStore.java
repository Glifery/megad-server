package org.glifery.smarthome.adapter.memory;

import lombok.extern.slf4j.Slf4j;
import org.glifery.smarthome.application.port.EventSourceInterface;
import org.glifery.smarthome.application.port.EventStoreInterface;
import org.glifery.smarthome.domain.model.event.AbstractEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Slf4j
@Component
public class InMemoryEventSourceStore implements EventSourceInterface, EventStoreInterface {
    private final ApplicationEventPublisher publisher;

    private final LinkedList<AbstractEvent> events = new LinkedList<>();

    public InMemoryEventSourceStore(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void publish(AbstractEvent abstractEvent) {
        log.info(String.format("Publish %s event '%s'", abstractEvent.getClass().getSimpleName(), abstractEvent.getName()));

        events.add(abstractEvent);

        publisher.publishEvent(abstractEvent);
    }

    public List<AbstractEvent> findAll() {
        return events;
    }

    @Override
    public void clearByTtl() {
        throw new RuntimeException("Method clearByTtl is not implemented");
    }
}
