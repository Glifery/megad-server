package org.glifery.smarthome.adapter.memory;

import org.glifery.smarthome.application.port.EventRepositoryInterface;
import org.glifery.smarthome.domain.event.AbstractEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LatestEventRepository implements EventRepositoryInterface {
    private final Map<String, AbstractEvent> latestEvents = new HashMap<>();

    @Override
    public void add(AbstractEvent abstractEvent) {
        latestEvents.put(abstractEvent.getName(), abstractEvent);
    }

    @Override
    public AbstractEvent findLatestByName(String name) {
        return latestEvents.get(name);
    }

    @Override
    public List<AbstractEvent> findByNameDesc(String name, Integer limit) {
        return new ArrayList<AbstractEvent>(){{
            add(findLatestByName(name));
        }};
    }

    @Override
    public void clearByTtl() {
        //TODO: implement it
    }
}
