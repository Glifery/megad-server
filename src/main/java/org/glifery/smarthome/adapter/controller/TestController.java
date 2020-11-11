package org.glifery.smarthome.adapter.controller;

import lombok.AllArgsConstructor;
import org.glifery.smarthome.adapter.google.Spreadsheet;
import org.glifery.smarthome.application.service.Megad;
import org.glifery.smarthome.domain.model.megad.MegadId;
import org.glifery.smarthome.domain.model.megad.ActionsList;
import org.glifery.smarthome.domain.model.megad.Port;
import org.glifery.smarthome.domain.model.megad.SingleAction;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
public class TestController {
    private Megad megad;
    private Spreadsheet spreadsheet;

    @GetMapping("/test")
    public String test() {
        try {
            spreadsheet.read();
        } catch (Exception e) {
            e.printStackTrace();
        }


        String str = "d";

        MegadId megadId = new MegadId("megad2");
        Port port1 = new Port(megadId,10);
        Port port2 = new Port(megadId,1555);
        ActionsList operation = ActionsList.create(new SingleAction(port1, SingleAction.SWITCH), new SingleAction(port2, SingleAction.SWITCH));

        megad.sendCommand(operation);

        return str;
    }
}
