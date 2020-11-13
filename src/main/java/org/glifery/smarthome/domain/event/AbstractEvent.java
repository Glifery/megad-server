package org.glifery.smarthome.domain.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public abstract class AbstractEvent {
    protected final String name;
    protected final LocalDateTime dateTime;
    protected final Duration ttl;

    public boolean wasBeforeThan(LocalDateTime compareDateTime) {
        return dateTime.isBefore(compareDateTime);
    }
}
