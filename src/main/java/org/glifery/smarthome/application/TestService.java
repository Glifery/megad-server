package org.glifery.smarthome.application;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
public class TestService {
    @Getter
    private Integer counter = 0;

    public void increment() {
        counter++;
    }
}
