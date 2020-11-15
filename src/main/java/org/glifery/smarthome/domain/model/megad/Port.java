package org.glifery.smarthome.domain.model.megad;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.lang.NonNull;

@Getter
@RequiredArgsConstructor
public class Port {
    private static final Integer MIN_PORT = 7;
    private static final Integer MAX_PORT = 13;

    @NonNull
    private final MegadId megadId;

    @NonNull
    @Range(min = 0, max = 28)
    private final Integer number;

    public static Port create(String megadId, Integer port) {
        return new Port(new MegadId(megadId), port);
    }

    public boolean equals(Port port) {
        return this.megadId.equals(port.getMegadId()) && this.number.equals(port.getNumber());
    }

    public String toString() {
        return number.toString();
    }
}
