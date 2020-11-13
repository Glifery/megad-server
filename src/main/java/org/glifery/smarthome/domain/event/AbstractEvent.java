package org.glifery.smarthome.domain.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public abstract class AbstractEvent {
    protected final String name;
    protected LocalDateTime dateTime = LocalDateTime.now();

    public boolean wasBeforeThan(LocalDateTime compareDateTime) {
        return dateTime.isBefore(compareDateTime);
    }
}
