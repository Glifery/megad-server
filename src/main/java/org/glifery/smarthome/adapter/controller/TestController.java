package org.glifery.smarthome.adapter.controller;

import lombok.AllArgsConstructor;
import org.glifery.smarthome.application.TestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class TestController {
    private TestService testService;

    @GetMapping("/test")
    public String test() {
        return "ind";
    }
}
