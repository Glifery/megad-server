package org.glifery.smarthome.domain.model.megad;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;

import javax.validation.Valid;

@Getter
@RequiredArgsConstructor
public abstract class IncomingRequest {
    @Valid
    @NonNull
    protected final Port port;
}
