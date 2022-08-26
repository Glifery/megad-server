package org.glifery.smarthome.adapter.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.glifery.smarthome.adapter.controller.request.PostActionEventRequest;
import org.glifery.smarthome.application.port.ControllerRepositoryInterface;
import org.glifery.smarthome.application.port.EventSourceInterface;
import org.glifery.smarthome.application.port.EventStoreInterface;
import org.glifery.smarthome.application.port.PortRepositoryInterface;
import org.glifery.smarthome.domain.event.aggregate.AllFromDateAggregate;
import org.glifery.smarthome.domain.event.aggregate.PortStateAggregate;
import org.glifery.smarthome.domain.event.aggregate.PortStatesAggregate;
import org.glifery.smarthome.domain.model.event.AbstractEvent;
import org.glifery.smarthome.domain.model.event.ActionEvent;
import org.glifery.smarthome.domain.model.megad.MegaD;
import org.glifery.smarthome.domain.model.megad.Port;
import org.glifery.smarthome.domain.model.megad.PortState;
import org.glifery.smarthome.domain.model.megad.SingleAction;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@AllArgsConstructor
public class ApiController {
    private final ControllerRepositoryInterface controllerRepository;
    private final PortRepositoryInterface portRepository;
    private final EventSourceInterface eventSource;
    private final EventStoreInterface eventStore;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/api/ports",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<Port> getPorts() {
        return portRepository.findAllPorts();
    }

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/api/ports/states",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Map<Port, PortState> getPortStates() {
        PortStatesAggregate portStatesAggregate = new PortStatesAggregate(portRepository.findAllPorts());
        portStatesAggregate.load(eventSource);

        return portStatesAggregate.getCurrentStates();
    }

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/api/ports/states/{megaD}/{port}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public PortState getPortState(
            @PathVariable MegaD megaD,
            @PathVariable Integer port
    ) {
        PortStateAggregate portStateAggregate = new PortStateAggregate(portRepository.findPort(megaD, port));
        portStateAggregate.load(eventSource);

        return portStateAggregate.getCurrentState();
    }

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/api/events",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<AbstractEvent> getEvents(
            @RequestParam(name = "start_date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate
    ) {
        AllFromDateAggregate aggregate = new AllFromDateAggregate(
                Optional.ofNullable(startDate).orElse(LocalDateTime.now().minusMinutes(1))
        );
        aggregate.load(eventSource);

        return aggregate.getAllByDateAsc();
    }

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/api/events/action",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public void postActionEvent(
            @RequestBody PostActionEventRequest postActionEventRequest
    ) {
        MegaD megaD = controllerRepository.findMegaD(postActionEventRequest.getMegaD());
        Port port = portRepository.findPort(megaD, postActionEventRequest.getPort());

        eventStore.publish(new ActionEvent(new SingleAction(port, postActionEventRequest.getAction()), LocalDateTime.now()));
    }
}
