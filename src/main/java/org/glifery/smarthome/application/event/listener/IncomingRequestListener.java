package org.glifery.smarthome.application.event.listener;

import lombok.extern.slf4j.Slf4j;
import org.glifery.smarthome.application.configuration.ApplicationConfig;
import org.glifery.smarthome.application.port.EventSourceInterface;
import org.glifery.smarthome.application.port.EventStoreInterface;
import org.glifery.smarthome.domain.event.aggregate.NameAggregate;
import org.glifery.smarthome.domain.model.event.AbstractEvent;
import org.glifery.smarthome.domain.model.event.ActionIncomingRequestEvent;
import org.glifery.smarthome.domain.model.event.ClickEvent;
import org.glifery.smarthome.domain.model.megad.ActionIncomingRequest;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/*
This handler converts standard PRESS event to various CLICK events

+CLICK - any click (PRESS)
+CLICK.FIRST - first click with any clicks after (PRESS if no other PRESS before)
CLICK.SINGLE - single click, no click after (near after CLICK_FIRST if no CLICK_DOUBLE so far)
+CLICK.DOUBLE - double click (PRESS with PRESS near before)
+CLICK.TRIPLE - triple click (PRESS with 2 PRESSes near before)
+CLICK.HOLD - hold after click (near after PRESS if no RELEASE so far)
+UNCLICK - any unclick (RELEASE)
 */
@Slf4j
@Component
public class IncomingRequestListener extends AbstractPublishingListener {
    private final ApplicationConfig config;
    private final EventSourceInterface eventSource;

    public IncomingRequestListener(ApplicationConfig config, EventStoreInterface eventStore, EventSourceInterface eventSource) {
        super(eventStore);
        this.config = config;
        this.eventSource = eventSource;
    }

    @EventListener
    public void checkBaseClicks(ActionIncomingRequestEvent event) {
        if (event.getRequest().getMode() != ActionIncomingRequest.Mode.PRESS) {
            return;
        }

        handleLog(event);

        publishClickEvent(ClickEvent.Type.CLICK, event);

        NameAggregate nameAggregate = new NameAggregate(event.getRequest().toString(), 3);
        nameAggregate.load(eventSource);

        LocalDateTime maxTimeForDoubleClick = event.getDateTime().minus(Duration.ofMillis(config.getDoubleClickMilliseconds()));
        List<AbstractEvent> lastEventsWithinPeriod = nameAggregate.getAllByDateDesc().stream()
                .filter(abstractEvent -> abstractEvent.wasNotBefore(maxTimeForDoubleClick))
                .collect(Collectors.toList());

        switch (lastEventsWithinPeriod.size()) {
            case 1: publishClickEvent(ClickEvent.Type.CLICK_FIRST, event); break;
            case 2: publishClickEvent(ClickEvent.Type.CLICK_DOUBLE, event); break;
            case 3: publishClickEvent(ClickEvent.Type.CLICK_TRIPLE, event); break;
        }
    }

    @EventListener
    public void checkUnclick(ActionIncomingRequestEvent event) {
        if (event.getRequest().getMode() != ActionIncomingRequest.Mode.RELEASE) {
            return;
        }

        handleLog(event);

        publishClickEvent(ClickEvent.Type.UNCLICK, event);
    }

    @EventListener
    public void checkSingleClick(ClickEvent event) throws InterruptedException {
        if (!event.getName().contains(ClickEvent.Type.CLICK_FIRST.toString())) {
            return;
        }

        Duration delay = Duration.ofMillis(config.getDoubleClickMilliseconds());

        Thread.sleep(delay.toMillis());
        LocalDateTime currentTime = LocalDateTime.now();

        handleLog(event, delay);

        String latestDoubleClickEventName = event.getName().replace(ClickEvent.Type.CLICK_FIRST.toString(), ClickEvent.Type.CLICK_DOUBLE.toString());
        NameAggregate nameAggregate = new NameAggregate(latestDoubleClickEventName, 1);
        nameAggregate.load(eventSource);

        AbstractEvent latestDoubleClickEventWithinPeriod = nameAggregate.getAllByDateDesc().stream()
                .findFirst()
                .filter(abstractEvent -> abstractEvent.wasNotBefore(event.getDateTime()))
                .orElse(null);

        if (Objects.isNull(latestDoubleClickEventWithinPeriod)) {
            publishClickEvent(ClickEvent.Type.CLICK_SINGLE, event.getActionIncomingRequestEvent(), currentTime);
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
        NameAggregate nameAggregate = new NameAggregate(latestReleaseEventName, 1);
        nameAggregate.load(eventSource);

        AbstractEvent latestReleaseEventWithinPeriod = nameAggregate.getAllByDateDesc().stream()
                .findFirst()
                .filter(abstractEvent -> abstractEvent.wasNotBefore(event.getDateTime()))
                .orElse(null);

        if (Objects.isNull(latestReleaseEventWithinPeriod)) {
            publishClickEvent(ClickEvent.Type.CLICK_HOLD, event, currentTime);
        }
    }

    private void publishClickEvent(ClickEvent.Type clickType, ActionIncomingRequestEvent parentEvent) {
        publishClickEvent(clickType, parentEvent, parentEvent.getDateTime());
    }

    private void publishClickEvent(ClickEvent.Type clickType, ActionIncomingRequestEvent parentEvent, LocalDateTime dateTime) {
        String eventName = String.format("%s.%s", parentEvent.getRequest().getPort(), clickType);

        publishAndLog(new ClickEvent(eventName, dateTime, parentEvent));
    }
}
