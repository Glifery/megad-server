package org.glifery.smarthome.domain.model.megad;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Range;
import org.springframework.lang.NonNull;

import javax.validation.Valid;

@Getter
@AllArgsConstructor
public class InputAction {
    public enum Mode {
        PRESS, RELEASE, HOLD
    }

    @Valid
    @NonNull
    private final Port port;

    @NonNull
    private final Mode mode;

    @NonNull
    @Range(min = 1, max = 2)
    private final Integer clickIndex;
}
