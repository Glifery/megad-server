package org.glifery.smarthome.domain.model.megad;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.lang.NonNull;

@RequiredArgsConstructor
public class PortAction {
    public static Integer OFF = 0;
    public static Integer ON = 1;
    public static Integer SWITCH = 2;
    public static Integer SYNC = 3;
    public static Integer SYNC_REVERSE = 4;

    @NonNull
    @Getter
    private final Port port;
    @NonNull
    @Range(min = 0, max = 4)
    private final Integer action;

    public static PortAction create(String megadId, Integer port, Integer action) {
        return new PortAction(new Port(new MegadId(megadId), port), action);
    }

    public String toString() {
        return String.format("%s:%s", port, action);
    }
}
