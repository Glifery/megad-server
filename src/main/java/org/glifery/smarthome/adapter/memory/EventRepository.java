package org.glifery.smarthome.adapter.memory;

import org.glifery.smarthome.application.port.EventRepositoryInterface;
import org.glifery.smarthome.domain.event.AbstractEvent;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class EventRepository implements EventRepositoryInterface {
    private final Map<String, AbstractEvent> latestEvents = new HashMap<>();

    @Override
    public void add(AbstractEvent abstractEvent) {
        latestEvents.put(abstractEvent.getName(), abstractEvent);
    }

    @Override
    public AbstractEvent findLatestByName(String name) {
        return latestEvents.get(name);
    }
}
