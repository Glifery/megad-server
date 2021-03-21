package org.glifery.smarthome.application.event.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.glifery.smarthome.application.port.EventRepositoryInterface;
import org.glifery.smarthome.domain.event.AbstractEvent;

@Slf4j
@RequiredArgsConstructor
public class AbstractPublishingListener extends AbstractListener {
    protected final EventRepositoryInterface eventRepository;

    protected void publishAndLog(AbstractEvent event) {
        log.info(String.format("Publish %s event (%s)", event.getName(), event.getDateTime()));

        eventRepository.publish(event);
    }
}
