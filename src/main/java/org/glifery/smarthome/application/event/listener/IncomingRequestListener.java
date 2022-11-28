package org.glifery.smarthome.application.event.listener;

import org.glifery.smarthome.application.configuration.ApplicationConfig;
import org.glifery.smarthome.application.port.EventSourceInterface;
import org.glifery.smarthome.application.port.EventStoreInterface;
import org.glifery.smarthome.domain.event.aggregate.SameEventLatestAggregate;
import org.glifery.smarthome.domain.model.event.ActionIncomingRequestEvent;
import org.glifery.smarthome.domain.model.event.ClickEvent;
import org.glifery.smarthome.domain.model.megad.ActionIncomingRequest;
import org.glifery.smarthome.domain.model.megad.Port;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

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
@Component
public class IncomingRequestListener extends AbstractPublishingListener {
    private final ApplicationConfig config;
    private final EventSourceInterface eventSource;

    @Value("${application.listeners.IncomingRequestListener.enabled}")
    private boolean enabled;

    public IncomingRequestListener(ApplicationConfig config, EventStoreInterface eventStore, EventSourceInterface eventSource) {
        super(eventStore);
        this.config = config;
        this.eventSource = eventSource;
    }

    @Override
    public String getListenerDescription() {
        return "This handler converts standard PRESS event to various CLICK events";
    }

    @Override
    public boolean getEnable() {
        return enabled;
    }

    @Override
    public void setEnable(boolean enable) {
        enabled = enable;
    }

    @EventListener
    public void checkBaseClicks(ActionIncomingRequestEvent event) {
        if (!getEnable()) {
            return;
        }

        if (event.getRequest().getMode() != ActionIncomingRequest.Mode.PRESS) {
            return;
        }

        handleLog(event);

        ClickEvent clickEvent = publishClickEvent(ClickEvent.Type.CLICK, event);

        LocalDateTime minDateTimeForDoubleClick = event.getEventDateTime().minus(Duration.ofMillis(config.getDoubleClickMilliseconds()));

        SameEventLatestAggregate previousClicksAggregate = new SameEventLatestAggregate(clickEvent, minDateTimeForDoubleClick, 3);
        previousClicksAggregate.load(eventSource);

        switch (previousClicksAggregate.size()) {
            case 1: publishClickEvent(ClickEvent.Type.CLICK_FIRST, event); break;
            case 2: publishClickEvent(ClickEvent.Type.CLICK_DOUBLE, event); break;
            case 3: publishClickEvent(ClickEvent.Type.CLICK_TRIPLE, event); break;
        }
    }

    @EventListener
    public void checkUnclick(ActionIncomingRequestEvent event) {
        if (!getEnable()) {
            return;
        }

        if (event.getRequest().getMode() != ActionIncomingRequest.Mode.RELEASE) {
            return;
        }

        handleLog(event);

        publishClickEvent(ClickEvent.Type.UNCLICK, event);
    }

    @EventListener
    public void checkSingleClick(ClickEvent event) throws InterruptedException {
        if (!getEnable()) {
            return;
        }

        if (!event.getName().contains(ClickEvent.Type.CLICK_FIRST.toString())) {
            return;
        }

        Duration delay = Duration.ofMillis(config.getDoubleClickMilliseconds());

        Thread.sleep(delay.toMillis());
        LocalDateTime currentTime = LocalDateTime.now();

        handleLog(event, delay);

        ClickEvent doubleClickEvent = new ClickEvent(ClickEvent.Type.CLICK_DOUBLE, event.getPort(), event.getEventDateTime());
        SameEventLatestAggregate previousDoubleClickAggregate = new SameEventLatestAggregate(doubleClickEvent, event.getEventDateTime(), 1);
        previousDoubleClickAggregate.load(eventSource);

        if (previousDoubleClickAggregate.size().equals(0)) {
            publishClickEvent(ClickEvent.Type.CLICK_SINGLE, event.getPort(), currentTime);
        }
    }

    @EventListener
    public void checkHoldClick(ActionIncomingRequestEvent event) throws InterruptedException {
        if (!getEnable()) {
            return;
        }

        if (event.getRequest().getMode() != ActionIncomingRequest.Mode.PRESS) {
            return;
        }

        Duration delay = Duration.ofMillis(config.getHoldClickMilliseconds());

        Thread.sleep(delay.toMillis());
        LocalDateTime currentTime = LocalDateTime.now();

        handleLog(event, delay);

        ActionIncomingRequestEvent releaseEvent = new ActionIncomingRequestEvent(new ActionIncomingRequest(event.getRequest().getPort(), ActionIncomingRequest.Mode.RELEASE, ActionIncomingRequest.Type.SINGLE));
        SameEventLatestAggregate previousReleaseAggregate = new SameEventLatestAggregate(releaseEvent, event.getEventDateTime(), 1);
        previousReleaseAggregate.load(eventSource);

        if (previousReleaseAggregate.size().equals(0)) {
            publishClickEvent(ClickEvent.Type.CLICK_HOLD, event.getRequest().getPort(), currentTime);
        }
    }

    private ClickEvent publishClickEvent(ClickEvent.Type clickType, ActionIncomingRequestEvent actionIncomingRequestEvent) {
        return publishClickEvent(clickType, actionIncomingRequestEvent.getRequest().getPort(), actionIncomingRequestEvent.getEventDateTime());
    }

    private ClickEvent publishClickEvent(ClickEvent.Type clickType, Port port, LocalDateTime dateTime) {
        ClickEvent clickEvent = new ClickEvent(clickType, port, dateTime);
        publishAndLog(clickEvent);

        return clickEvent;
    }
}
