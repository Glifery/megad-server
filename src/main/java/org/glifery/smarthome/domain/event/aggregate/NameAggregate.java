package org.glifery.smarthome.domain.event.aggregate;

import org.glifery.smarthome.domain.model.event.AbstractEvent;

public class NameAggregate extends BaseAccumulativeAggregate {
    private final String name;

    public NameAggregate(String name, Integer limit) {
        super(limit);
        this.name = name;
    }

    @Override
    protected boolean supports(AbstractEvent event) {
        return event.getName().equals(name);
    }
}
