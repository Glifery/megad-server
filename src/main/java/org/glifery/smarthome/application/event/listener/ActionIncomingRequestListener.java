package org.glifery.smarthome.application.event.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.glifery.smarthome.application.configuration.ApplicationConfig;
import org.glifery.smarthome.application.port.EventRepositoryInterface;
import org.glifery.smarthome.domain.event.AbstractEvent;
import org.glifery.smarthome.domain.event.ActionIncomingRequestEvent;
import org.glifery.smarthome.domain.event.ClickEvent;
import org.glifery.smarthome.domain.model.megad.ActionIncomingRequest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/*
+CLICK - any click (PRESS)
+CLICK.FIRST - first click with any clicks after (PRESS if no other PRESS before)
CLICK.SINGLE - single click, no click after (near after RELEASE if no PRESS so far)
+CLICK.DOUBLE - double click (PRESS with PRESS near before)
CLICK.HOLD - hold after click (near after PRESS if no RELEASE so far)
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ActionIncomingRequestListener {
    private final ApplicationConfig config;
    private final EventRepositoryInterface eventRepository;
    private final ApplicationEventPublisher publisher;

    @EventListener
    public void handleActionIncomingRequestEvent(ActionIncomingRequestEvent event) {
        if (event.getRequest().getMode() != ActionIncomingRequest.Mode.PRESS) {
            return;
        }

        LocalDateTime currentTime = LocalDateTime.now();
        log.info(String.format("Handle %s event", event.getRequest()));

        publishClickEvent(event, currentTime);

        AbstractEvent latestEvent = eventRepository.findLatestByName(event.getRequest().toString());
        eventRepository.add(event);

        if (Objects.isNull(latestEvent)) {
            publishClickFirstEvent(event, currentTime);

            return;
        }

        LocalDateTime maxTimeForDoubleClick = currentTime.minusNanos(config.getDoubleClickMilliseconds() * 1000000l);
        if (latestEvent.getDateTime().isAfter(maxTimeForDoubleClick)) {
            publishClickDoubleEvent(event, currentTime);

            return;
        }

        publishClickFirstEvent(event, currentTime);
    }

    private String generateClickEventName(ActionIncomingRequestEvent event, ClickEvent.Type clickType) {
        String baseName = Arrays.stream(event.getRequest().toString().split("\\.")).limit(2).collect(Collectors.joining("."));

        return String.format("%s.%s", baseName, clickType);
    }

    private void publishClickEvent(ActionIncomingRequestEvent event, LocalDateTime dateTime) {
        publish(generateClickEventName(event, ClickEvent.Type.CLICK), dateTime);

    }

    private void publishClickFirstEvent(ActionIncomingRequestEvent event, LocalDateTime dateTime) {
        publish(generateClickEventName(event, ClickEvent.Type.CLICK_FIRST), dateTime);
    }

    private void publishClickDoubleEvent(ActionIncomingRequestEvent event, LocalDateTime dateTime) {
        publish(generateClickEventName(event, ClickEvent.Type.CLICK_DOUBLE), dateTime);
    }

    private void publish(String eventName, LocalDateTime dateTime) {
        log.info(String.format("Publish %s event", eventName));

        publisher.publishEvent(new ClickEvent(eventName, dateTime));
    }
}
