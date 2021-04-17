package org.glifery.smarthome.domain.model.megad;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.lang.NonNull;

import javax.validation.Valid;

@Getter
@RequiredArgsConstructor
public class SingleAction {
    public static final Integer OFF = 0;
    public static final Integer ON = 1;
    public static final Integer SWITCH = 2;
    public static final Integer SYNC = 3;
    public static final Integer SYNC_REVERSE = 4;

    @Valid
    @NonNull
    private final Port port;

    @NonNull
    @Range(min = 0, max = 4)
    private final Integer action;

    public static SingleAction create(String megadId, Integer port, Integer action) {
        return new SingleAction(new Port(new MegadId(megadId), port), action);
    }

    public static SingleAction copyWithAction(SingleAction singleAction, Integer action) {
        return new SingleAction(singleAction.getPort(), action);
    }

    public String toString() {
        return String.format("%s:%s", port, action);
    }
}
