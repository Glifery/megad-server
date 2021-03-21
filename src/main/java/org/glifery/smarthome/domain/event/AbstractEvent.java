package org.glifery.smarthome.domain.event;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@RequiredArgsConstructor
public abstract class AbstractEvent {
    protected final String name;
    protected final LocalDateTime dateTime;
    protected final Duration ttl;

    public boolean wasBefore(LocalDateTime compareDateTime) {
        return dateTime.isBefore(compareDateTime);
    }

    public boolean wasNotBefore(LocalDateTime compareDateTime) {
        return !wasBefore(compareDateTime);
    }
}
