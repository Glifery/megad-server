package org.glifery.smarthome.domain.model.megad;

import lombok.Getter;
import org.springframework.lang.NonNull;

@Getter
public class ActionIncomingRequest extends IncomingRequest {
    public enum Mode {
        PRESS, RELEASE, HOLD
    }
    public enum ClickType {
        SINGLE, DOUBLE
    }

    public ActionIncomingRequest(Port port, Mode mode, ClickType clickType) {
        super(port);
        this.mode = mode;
        this.clickType = clickType;
    }

    @NonNull
    private final Mode mode;

    @NonNull
    private final ClickType clickType;

    public String toString() {
        return String.format("%s.%s.%s", port, mode, clickType);
    }
}
