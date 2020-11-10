package org.glifery.smarthome.adapter.controller;

import lombok.AllArgsConstructor;
import org.glifery.smarthome.application.service.Megad;
import org.glifery.smarthome.domain.model.megad.MegadId;
import org.glifery.smarthome.domain.model.megad.Operation;
import org.glifery.smarthome.domain.model.megad.Port;
import org.glifery.smarthome.domain.model.megad.PortAction;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class TestController {
    private Megad megad;

    @GetMapping("/test")
    public String test() {
        String str = "d";

        MegadId megadId = new MegadId("megad2");
        Port port1 = new Port(megadId,10);
        Port port2 = new Port(megadId,1555);
        Operation operation = Operation.create(new PortAction(port1, PortAction.SWITCH), new PortAction(port2, PortAction.SWITCH));

        megad.sendCommand(operation);

        return str;
    }
}
