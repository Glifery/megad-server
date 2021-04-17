package org.glifery.smarthome.domain.event.aggregate;

import org.glifery.smarthome.domain.model.event.AbstractEvent;

import java.time.LocalDateTime;

public class AllFromDateAggregate extends BaseAccumulativeAggregate {
    private final LocalDateTime startDate;

    public AllFromDateAggregate(LocalDateTime startDate) {
        super();
        this.startDate = startDate;
    }

    @Override
    protected boolean supports(AbstractEvent event) {
        return event.getDateTime().isAfter(startDate);
    }
}
