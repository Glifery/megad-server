package org.glifery.smarthome.domain.event.aggregate;

import org.glifery.smarthome.domain.event.BaseAggregate;
import org.glifery.smarthome.domain.model.event.AbstractEvent;

import java.util.*;
import java.util.stream.Collectors;

public abstract class BaseAccumulativeAggregate extends BaseAggregate {
    protected final List<AbstractEvent> events;
    private final Integer lastAmount;

    public BaseAccumulativeAggregate() {
        this.events = new ArrayList<>();
        this.lastAmount = null;
    }

    public BaseAccumulativeAggregate(Integer lastAmount) {
        this.events = new ArrayList<>();
        this.lastAmount = lastAmount;
    }

    @Override
    protected void on(AbstractEvent event) {
        events.add(event);

        if (Objects.nonNull(lastAmount) && events.size() > lastAmount) {
            events.remove(0);
        }
    }

    public Integer size() {
        return events.size();
    }

    public Optional<AbstractEvent> getLast() {
        return events.size() > 0 ? Optional.of(events.get(events.size() - 1)) : Optional.empty();
    }

    public List<AbstractEvent> getAllByDateAsc() {
        return events.stream()
                .sorted(Comparator.comparing(AbstractEvent::getEventDateTime))
                .collect(Collectors.toList());
    }

    public List<AbstractEvent> getAllByDateDesc() {
        return events.stream()
                .sorted(Comparator.comparing(AbstractEvent::getEventDateTime).reversed())
                .collect(Collectors.toList());
    }
}
