package org.glifery.smarthome.adapter.controller;

import org.glifery.smarthome.adapter.controller.util.MegadInputActionConverter;
import org.glifery.smarthome.domain.model.megad.InputAction;
import org.glifery.smarthome.domain.model.megad.MegadId;
import org.glifery.smarthome.domain.model.megad.SingleAction;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class MegadController {

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/server/{megadId}",
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public String input(
            @PathVariable MegadId megadId,
            @RequestParam(name = "pt", required = true) Integer port,
            @RequestParam(name = "m", required = false) Integer mode,
            @RequestParam(name = "click", required = false) Integer clickIndex
    ) {
        InputAction inputAction = MegadInputActionConverter.createFromServerRequest(megadId, port, mode, clickIndex);

        return SingleAction.create(megadId.toString(), port, 2).toString();
    }
}
