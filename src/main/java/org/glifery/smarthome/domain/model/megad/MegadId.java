package org.glifery.smarthome.domain.model.megad;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MegadId {
    private final String megadId;

    public boolean equals(MegadId megadId) {
        return this.megadId.equals(megadId.toString());
    }

    public String toString() {
        return megadId;
    }
}
