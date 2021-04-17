package org.glifery.smarthome.domain.event;

import lombok.NoArgsConstructor;
import org.glifery.smarthome.application.port.EventSourceInterface;
import org.glifery.smarthome.domain.model.event.AbstractEvent;

@NoArgsConstructor
public abstract class BaseAggregate {
    public void load(EventSourceInterface eventSource) {
        eventSource.findAll().stream()
                .filter(this::supports)
                .forEach(this::on);
    }

    protected abstract boolean supports(AbstractEvent event);
    protected abstract void on(AbstractEvent event);
}
