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

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/*
+CLICK - any click (PRESS)
+CLICK.FIRST - first click with any clicks after (PRESS if no other PRESS before)
+CLICK.SINGLE - single click, no click after (near after RELEASE if no PRESS so far and before)
+CLICK.DOUBLE - double click (PRESS with PRESS near before)
CLICK.TRIPLE - triple click (PRESS with 2 PRESSes near before)
+CLICK.HOLD - hold after click (near after PRESS if no RELEASE so far)
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
    public void checkBaseClicks(ActionIncomingRequestEvent event) {
        if (event.getRequest().getMode() == ActionIncomingRequest.Mode.PRESS) {
            handleLog(event);

            publishClickEvent(ClickEvent.Type.CLICK, event);

            List<AbstractEvent> lastEvents = eventRepository.findByNameDesc(event.getRequest().toString(), 2);
            LocalDateTime maxTimeForDoubleClick = event.getDateTime().minus(Duration.ofMillis(config.getDoubleClickMilliseconds()));

            if (lastEvents.isEmpty() || lastEvents.get(0).wasBeforeThan(maxTimeForDoubleClick)) {
                publishClickEvent(ClickEvent.Type.CLICK_FIRST, event);
            } else {
                if ((lastEvents.size() == 1) || lastEvents.get(1).wasBeforeThan(maxTimeForDoubleClick)) {
                    publishClickEvent(ClickEvent.Type.CLICK_DOUBLE, event);
                } else {
                    publishClickEvent(ClickEvent.Type.CLICK_TRIPLE, event);
                }
            }
        }

        eventRepository.add(event);
    }

    @EventListener
    public void checkSingleClick(ActionIncomingRequestEvent event) throws InterruptedException {
        if (event.getRequest().getMode() != ActionIncomingRequest.Mode.RELEASE) {
            return;
        }

        Duration delay = Duration.ofMillis(config.getHoldClickMilliseconds()).dividedBy(2);

        Thread.sleep(delay.toMillis());
        LocalDateTime currentTime = LocalDateTime.now();

        handleLog(event, delay);

        String latestPressEventName = String.format("%s.%s.%s", event.getRequest().getPort(), ActionIncomingRequest.Mode.PRESS, ActionIncomingRequest.ClickType.SINGLE);
        AbstractEvent latestPressEvent = eventRepository.findLatestByName(latestPressEventName);

        if (Objects.isNull(latestPressEvent) || latestPressEvent.wasBeforeThan(event.getDateTime().minus(Duration.ofMillis(config.getDoubleClickMilliseconds())))) {
            publishClickEvent(ClickEvent.Type.CLICK_SINGLE, event, currentTime);
        }
    }

    @EventListener
    public void checkHoldClick(ActionIncomingRequestEvent event) throws InterruptedException {
        if (event.getRequest().getMode() != ActionIncomingRequest.Mode.PRESS) {
            return;
        }

        Duration delay = Duration.ofMillis(config.getHoldClickMilliseconds());

        Thread.sleep(delay.toMillis());
        LocalDateTime currentTime = LocalDateTime.now();

        handleLog(event, delay);

        String latestReleaseEventName = String.format("%s.%s.%s", event.getRequest().getPort(), ActionIncomingRequest.Mode.RELEASE, ActionIncomingRequest.ClickType.SINGLE);
        AbstractEvent latestReleaseEvent = eventRepository.findLatestByName(latestReleaseEventName);

        if (Objects.isNull(latestReleaseEvent) || latestReleaseEvent.wasBeforeThan(event.getDateTime())) {
            publishClickEvent(ClickEvent.Type.CLICK_HOLD, event, currentTime);
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
