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
import java.util.Objects;

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
public class IncomingRequestListener {
    private final ApplicationConfig config;
    private final EventRepositoryInterface eventRepository;
    private final ApplicationEventPublisher publisher;

    @EventListener
    public void convertActionToClick(ActionIncomingRequestEvent event) {
        if (event.getRequest().getMode() == ActionIncomingRequest.Mode.PRESS) {
            log.info(String.format("Handle %s event", event.getRequest()));

            publishClickEvent(event);

            AbstractEvent latestEvent = eventRepository.findLatestByName(event.getRequest().toString());
            LocalDateTime maxTimeForDoubleClick = event.getDateTime().minusNanos(config.getDoubleClickMilliseconds() * 1000000l);

            if (Objects.isNull(latestEvent) || latestEvent.wasBeforeThan(maxTimeForDoubleClick)) {
                publishClickFirstEvent(event);
            } else {
                publishClickDoubleEvent(event);
            }
        }

        eventRepository.add(event);
    }

    @EventListener
    public void convertActionToClickWithDelay(ActionIncomingRequestEvent event) throws InterruptedException {
        if (event.getRequest().getMode() == ActionIncomingRequest.Mode.HOLD) {
            return;
        }

        Thread.sleep(config.getHoldClickMilliseconds());

        log.info(String.format("Handle %s event with delay %ss", event.getRequest(), config.getHoldClickMilliseconds() / 1000));

        if (event.getRequest().getMode() == ActionIncomingRequest.Mode.PRESS) {
            String latestReleaseEventName = String.format("%s.%s.%s", event.getRequest().getPort(), ActionIncomingRequest.Mode.RELEASE, ActionIncomingRequest.ClickType.SINGLE);
            AbstractEvent latestReleaseEvent = eventRepository.findLatestByName(latestReleaseEventName);

            if (Objects.isNull(latestReleaseEvent) || latestReleaseEvent.wasBeforeThan(event.getDateTime())) {
                publishClickHoldEvent(event);
            }

            return;
        }
    }

    private void publishClickEvent(ActionIncomingRequestEvent event) {
        publish(String.format("%s.%s", event.getRequest().getPort(), ClickEvent.Type.CLICK), event.getDateTime());

    }

    private void publishClickFirstEvent(ActionIncomingRequestEvent event) {
        publish(String.format("%s.%s", event.getRequest().getPort(), ClickEvent.Type.CLICK_FIRST), event.getDateTime());
    }

    private void publishClickDoubleEvent(ActionIncomingRequestEvent event) {
        publish(String.format("%s.%s", event.getRequest().getPort(), ClickEvent.Type.CLICK_DOUBLE), event.getDateTime());
    }

    private void publishClickHoldEvent(ActionIncomingRequestEvent event) {
        publish(String.format("%s.%s", event.getRequest().getPort(), ClickEvent.Type.CLICK_HOLD), event.getDateTime());
    }

    private void publish(String eventName, LocalDateTime dateTime) {
        log.info(String.format("Publish %s event", eventName));

        publisher.publishEvent(new ClickEvent(eventName, dateTime));
    }
}
