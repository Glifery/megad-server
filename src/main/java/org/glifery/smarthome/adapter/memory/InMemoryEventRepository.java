package org.glifery.smarthome.adapter.memory;

import org.glifery.smarthome.application.port.EventRepositoryInterface;
import org.glifery.smarthome.domain.event.AbstractEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class InMemoryEventRepository implements EventRepositoryInterface {
    private final Map<String, LinkedList<AbstractEvent>> events = new HashMap<>();

    @Override
    public void add(AbstractEvent abstractEvent) {
        getEventsList(abstractEvent.getName()).add(abstractEvent);
    }

    @Override
    public AbstractEvent findLatestByName(String name) {
        LinkedList<AbstractEvent> eventsList = getEventsList(name);

        if (eventsList.isEmpty()) {
            return null;
        }

        return eventsList.getLast();
    }

    @Override
    public List<AbstractEvent> findByNameDesc(String name, Integer limit) {
        LinkedList<AbstractEvent> eventsList = getEventsList(name);

        if (eventsList.isEmpty()) {
            return new ArrayList<>();
        }

        List<AbstractEvent> subList = new ArrayList<>(eventsList.subList(eventsList.size() - Math.min(eventsList.size(), limit), eventsList.size()));

        Collections.reverse(subList);

        return subList;
    }

    @Override
    public void clearByTtl() {
        events.entrySet().stream().forEach(eventsList -> {
            Iterator<AbstractEvent> iterator = eventsList.getValue().iterator();

            while (iterator.hasNext()) {
                AbstractEvent event = iterator.next();

                if (event.wasBefore(LocalDateTime.now().minus(event.getTtl()))) {
                    iterator.remove();
                } else {
                    break;
                }
            }
        });
    }

    private LinkedList<AbstractEvent> getEventsList(String name) {
        if (!events.containsKey(name)) {
            events.put(name, new LinkedList<>());
        }

        return events.get(name);
    }
}
