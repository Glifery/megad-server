package org.glifery.smarthome.domain.model.megad;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class MegadId {
    private final String megadId;

    public boolean equals(MegadId megadId) {
        return this.megadId.equals(megadId.toString());
    }

    public String toString() {
        return megadId;
    }
}
