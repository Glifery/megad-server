package org.glifery.smarthome.application.event.listener;

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
@Component
public class IncomingRequestListener extends AbstractPublishingListener {
    private final ApplicationConfig config;
    private final EventRepositoryInterface eventRepository;

    public IncomingRequestListener(ApplicationEventPublisher publisher, ApplicationConfig config, EventRepositoryInterface eventRepository) {
        super(publisher);
        this.config = config;
        this.eventRepository = eventRepository;
    }

    @EventListener
    public void convertActionToClick(ActionIncomingRequestEvent event) {
        if (event.getRequest().getMode() == ActionIncomingRequest.Mode.PRESS) {
            handleLog(event);

            publishClickEvent(ClickEvent.Type.CLICK, event);

            AbstractEvent latestEvent = eventRepository.findLatestByName(event.getRequest().toString());
            LocalDateTime maxTimeForDoubleClick = event.getDateTime().minusNanos(config.getDoubleClickMilliseconds() * 1000000l);

            if (Objects.isNull(latestEvent) || latestEvent.wasBeforeThan(maxTimeForDoubleClick)) {
                publishClickEvent(ClickEvent.Type.CLICK_FIRST, event);
            } else {
                publishClickEvent(ClickEvent.Type.CLICK_DOUBLE, event);
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
        LocalDateTime currentTime = LocalDateTime.now();

        handleLog(event, config.getHoldClickMilliseconds() / 1000);

        if (event.getRequest().getMode() == ActionIncomingRequest.Mode.PRESS) {
            String latestReleaseEventName = String.format("%s.%s.%s", event.getRequest().getPort(), ActionIncomingRequest.Mode.RELEASE, ActionIncomingRequest.ClickType.SINGLE);
            AbstractEvent latestReleaseEvent = eventRepository.findLatestByName(latestReleaseEventName);

            if (Objects.isNull(latestReleaseEvent) || latestReleaseEvent.wasBeforeThan(event.getDateTime())) {
                publishClickEvent(ClickEvent.Type.CLICK_HOLD, event, currentTime);
            }

            return;
        }
    }

    private void publishClickEvent(ClickEvent.Type clickType, ActionIncomingRequestEvent parentEvent) {
        publishClickEvent(clickType, parentEvent, parentEvent.getDateTime());
    }

    private void publishClickEvent(ClickEvent.Type clickType, ActionIncomingRequestEvent parentEvent, LocalDateTime dateTime) {
        String eventName = String.format("%s.%s", parentEvent.getRequest().getPort(), clickType);

        publishAndLog(new ClickEvent(eventName, dateTime));
    }
}
