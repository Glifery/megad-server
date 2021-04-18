package org.glifery.smarthome.domain.model.megad;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.lang.NonNull;

@Getter
@RequiredArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Port {
    private static final Integer MIN_PORT = 7;
    private static final Integer MAX_PORT = 13;

    @NonNull
    private final MegaD megaD;

    @NonNull
    @Range(min = 0, max = 28)
    private final Integer number;

    private final String title;

    public String toString() {
        return String.format("%s.%s", megaD, number);
    }
}
