package org.glifery.smarthome.application.event.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.glifery.smarthome.domain.event.AbstractEvent;
import org.springframework.context.ApplicationEventPublisher;

@Slf4j
@RequiredArgsConstructor
public class AbstractPublishingListener extends AbstractListener {
    private final ApplicationEventPublisher publisher;

    protected void publishAndLog(AbstractEvent event) {
        log.info(String.format("Publish %s event (%s)", event.getName(), event.getDateTime()));

        publisher.publishEvent(event);
    }
}
