package org.glifery.smarthome.domain.model.megad;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.lang.NonNull;

@RequiredArgsConstructor
public class Port {
    private static final Integer MIN_PORT = 7;
    private static final Integer MAX_PORT = 13;

    @NonNull
    @Getter
    private final MegadId megadId;

    @NonNull
    @Range(min = 7, max = 13)
    private final Integer number;

    public String toString() {
        return number.toString();
    }
}
