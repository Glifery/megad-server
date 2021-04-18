package org.glifery.smarthome.domain.model.megad;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;

@Getter
@AllArgsConstructor
public class PortState {
    public enum State {
        ON,
        OFF
    }

    @NonNull
    private final Port port;

    @NonNull
    @Setter
    private State state;

    public static PortState create(Port port, State state) {
        return new PortState(port, state);
    }

    public String toString() {
        return String.format("%s:%s", port, state);
    }
}
