package org.glifery.smarthome.domain.model.megad;

import lombok.Getter;
import org.springframework.lang.NonNull;

@Getter
public class ActionIncomingRequest extends IncomingRequest {
    public enum Mode {
        PRESS, RELEASE, HOLD
    }
    public enum Type {
        SINGLE, DOUBLE
    }

    public ActionIncomingRequest(Port port, Mode mode, Type type) {
        super(port);
        this.mode = mode;
        this.type = type;
    }

    @NonNull
    private final Mode mode;

    @NonNull
    private final Type type;

    public String toString() {
        return String.format("%s.%s.%s", port, mode, type);
    }
}
