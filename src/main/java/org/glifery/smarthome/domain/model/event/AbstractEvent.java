package org.glifery.smarthome.domain.model.event;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@RequiredArgsConstructor
public abstract class AbstractEvent {
    protected final UUID uuid = UUID.randomUUID();
    protected final LocalDateTime createDateTime = LocalDateTime.now();
    protected final String name;
    protected final LocalDateTime eventDateTime;
    protected final Duration ttl;

    public boolean wasBefore(LocalDateTime compareDateTime) {
        return eventDateTime.isBefore(compareDateTime);
    }

    public boolean wasNotBefore(LocalDateTime compareDateTime) {
        return !wasBefore(compareDateTime);
    }

    public String toString() {
        return name;
    }
}
