package org.glifery.smarthome.domain.model.megad;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;

import javax.validation.Valid;

@Getter
@RequiredArgsConstructor
public class PortActionsList {
    @Valid
    @NonNull
    private final Port port;

    @Valid
    @NonNull
    private final ActionsList actionsList;
}
