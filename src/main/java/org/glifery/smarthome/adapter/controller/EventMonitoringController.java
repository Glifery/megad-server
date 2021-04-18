package org.glifery.smarthome.adapter.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.glifery.smarthome.application.port.EventSourceInterface;
import org.glifery.smarthome.application.port.PortRepositoryInterface;
import org.glifery.smarthome.domain.event.aggregate.AllFromDateAggregate;
import org.glifery.smarthome.domain.model.event.AbstractEvent;
import org.glifery.smarthome.domain.model.megad.Port;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@AllArgsConstructor
public class EventMonitoringController {
    private final PortRepositoryInterface portRepository;
    private final EventSourceInterface eventSource;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/api/ports",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<Port> getPorts() {
        return portRepository.findAll();
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
}
