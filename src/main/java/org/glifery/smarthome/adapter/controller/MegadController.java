package org.glifery.smarthome.adapter.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.glifery.smarthome.adapter.controller.util.MegadIncomingRequestConverter;
import org.glifery.smarthome.application.configuration.ApplicationConfig;
import org.glifery.smarthome.application.port.PortActionsRepositoryInterface;
import org.glifery.smarthome.domain.event.ActionIncomingRequestEvent;
import org.glifery.smarthome.domain.model.megad.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Slf4j
@RestController
@AllArgsConstructor
public class MegadController {
    private static String empty = "";

    private final ApplicationConfig applicationConfig;
    private final PortActionsRepositoryInterface portActionsRepository;
    private final ApplicationEventPublisher publisher;

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

        log.info(String.format("HTTP request: %s", incomingRequest));

        if (incomingRequest instanceof ActionIncomingRequest) {

            if (applicationConfig.isDirectMegadResponse() && ifActionIsPress((ActionIncomingRequest) incomingRequest)) {
                return generateDirectResponse((ActionIncomingRequest) incomingRequest);
            }

            return publishRequest((ActionIncomingRequest) incomingRequest);
        }

        log.info(String.format("HTTP response for %s: empty", incomingRequest));

        return empty;
    }

    private boolean ifActionIsPress(ActionIncomingRequest incomingRequest) {
        return (incomingRequest.getMode() == ActionIncomingRequest.Mode.PRESS);
    }

    private String generateDirectResponse(ActionIncomingRequest incomingRequest) {
        ActionsList actionsList = portActionsRepository.getActionsList(incomingRequest.getPort());

        if (Objects.nonNull(actionsList)) {
            log.info(String.format("HTTP response for %s: %s", incomingRequest, actionsList));

            return actionsList.toString();
        }

        return empty;
    }

    private String publishRequest(ActionIncomingRequest incomingRequest) {
        publisher.publishEvent(new ActionIncomingRequestEvent(incomingRequest.toString(), incomingRequest));

        log.info(String.format("Publish %s event. HTTP response: empty", incomingRequest));

        return empty;
    }
}
