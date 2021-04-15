package org.glifery.smarthome.adapter.memory;

import lombok.extern.slf4j.Slf4j;
import org.glifery.smarthome.application.port.EventRepositoryInterface;
import org.glifery.smarthome.domain.model.event.AbstractEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryEventRepository implements EventRepositoryInterface {
    private final ApplicationEventPublisher publisher;

    private final Map<String, LinkedList<AbstractEvent>> events = new HashMap<>();

    public InMemoryEventRepository(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void publish(AbstractEvent abstractEvent) {
        log.info(String.format("Publishing event: %s", abstractEvent.getName()));

        getEventsList(abstractEvent.getName()).add(abstractEvent);

        publisher.publishEvent(abstractEvent);
    }

    @Override
    public Optional<AbstractEvent> findLatestByName(String name) {
        LinkedList<AbstractEvent> eventsList = getEventsList(name);

        if (eventsList.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(eventsList.getLast());
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

    public List<AbstractEvent> findAllAsc(LocalDateTime startDate) {
        return events.entrySet().stream()
                .flatMap(eventsEntry -> eventsEntry.getValue().stream())
                .filter(abstractEvent -> abstractEvent.getDateTime().isAfter(startDate))
                .sorted(Comparator.comparing(AbstractEvent::getDateTime))
                .collect(Collectors.toList());
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
