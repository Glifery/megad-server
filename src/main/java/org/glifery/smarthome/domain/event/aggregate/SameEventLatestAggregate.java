package org.glifery.smarthome.domain.event.aggregate;

import lombok.extern.slf4j.Slf4j;
import org.glifery.smarthome.domain.model.event.AbstractEvent;

import java.time.LocalDateTime;

public class SameEventLatestAggregate extends BaseAccumulativeAggregate {
    private final AbstractEvent sameEvent;
    private final LocalDateTime minDateTime;

    public SameEventLatestAggregate(AbstractEvent sameEvent, LocalDateTime minDateTime, Integer limit) {
        super(limit);
        this.sameEvent = sameEvent;
        this.minDateTime = minDateTime;
    }

    @Override
    protected boolean supports(AbstractEvent event) {
        return event.getName().equals(sameEvent.getName()) && event.wasNotBefore(minDateTime);
    }
}
