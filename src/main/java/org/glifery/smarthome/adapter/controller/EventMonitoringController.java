package org.glifery.smarthome.adapter.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.glifery.smarthome.application.port.EventRepositoryInterface;
import org.glifery.smarthome.domain.event.AbstractEvent;
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
    private final EventRepositoryInterface eventRepository;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/api/monitoring/events",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<AbstractEvent> update(
            @RequestParam(name = "start_date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate
    ) {
//        eventRepository.add(new ClickEvent("qqq", LocalDateTime.now()));
//        eventRepository.add(new ClickEvent("qqdasdsadaq", LocalDateTime.now()));

        return eventRepository.findAllAsc(Optional.ofNullable(startDate).orElse(LocalDateTime.now().minusMinutes(1)));
    }
}
