package org.glifery.smarthome.domain.model.megad;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class SingleAction {
//    public static final Integer OFF = 0;
//    public static final Integer ON = 1;
//    public static final Integer SWITCH = 2;
//    public static final Integer SYNC = 3;
//    public static final Integer SYNC_REVERSE = 4;

    public enum Action {
        OFF,
        ON,
        SWITCH,
        SYNC,
        SYNC_REVERSE
    }
    public static Map<Integer, Action> actionMap = new HashMap<Integer, Action>(){{
        put(0, Action.OFF);
        put(1, Action.ON);
        put(2, Action.SWITCH);
        put(3, Action.SYNC);
        put(4, Action.SYNC_REVERSE);
    }};

    @Valid
    @NonNull
    private final Port port;

    @NonNull
    private final Action action;

    public static SingleAction create(String megadId, Integer port, Action action) {
        return new SingleAction(new Port(new MegadId(megadId), port), action);
    }

    public static SingleAction copyWithAction(SingleAction singleAction, Action action) {
        return new SingleAction(singleAction.getPort(), action);
    }

    public String toString() {
        return String.format("%s:%s", port, action);
    }
}
