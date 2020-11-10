package org.glifery.smarthome.domain.model.megad;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MegadId {
    private final String megadId;

    public String toString() {
        return megadId;
    }
}
