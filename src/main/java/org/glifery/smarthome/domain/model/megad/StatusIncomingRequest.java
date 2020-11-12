package org.glifery.smarthome.domain.model.megad;

import lombok.Getter;
import org.springframework.lang.NonNull;

@Getter
public class StatusIncomingRequest extends IncomingRequest {
    public enum Status {
        ON, OFF
    }

    @NonNull
    private final Status status;

    public StatusIncomingRequest(Port port, Status status) {
        super(port);
        this.status = status;
    }

    public String toString() {
        return String.format("%s.%s", port, status);
    }
}
