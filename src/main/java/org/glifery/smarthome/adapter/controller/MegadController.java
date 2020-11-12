package org.glifery.smarthome.adapter.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.glifery.smarthome.adapter.controller.util.MegadIncomingRequestConverter;
import org.glifery.smarthome.application.port.PortActionsRepositoryInterface;
import org.glifery.smarthome.domain.model.megad.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Slf4j
@RestController
@AllArgsConstructor
public class MegadController {
    private final PortActionsRepositoryInterface portActionsRepository;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/server/{megadId}",
            produces = MediaType.TEXT_HTML_VALUE
    )
    public String input(
            @PathVariable MegadId megadId,
            @RequestParam(name = "pt", required = true) Integer port,
            @RequestParam(name = "click", required = false) ActionIncomingRequest.ClickType clickType,
            @RequestParam(name = "cnt", required = false) Integer clickCounter,
            @RequestParam(name = "m", required = false) ActionIncomingRequest.Mode mode,
            @RequestParam(name = "v", required = false) StatusIncomingRequest.Status portStatus,
            @RequestParam(name = "mdid", required = false) String mdid
            ) {
        IncomingRequest incomingRequest = MegadIncomingRequestConverter.createFromServerRequest(megadId, port, clickType, clickCounter, mode, portStatus);

        log.warn(String.format("Incoming request: %s", incomingRequest));

        if ((incomingRequest instanceof ActionIncomingRequest) && (((ActionIncomingRequest) incomingRequest).getMode() == ActionIncomingRequest.Mode.PRESS)) {
            ActionsList actionsList = portActionsRepository.getActionsList(incomingRequest.getPort());

            if (Objects.nonNull(actionsList)) {
                log.warn(String.format("Incoming request: %s. Output: %s", incomingRequest, actionsList));

                return actionsList.toString();
            }
        }

        log.warn(String.format("Incoming request: %s. Output: empty", incomingRequest));

        return "";
    }
}