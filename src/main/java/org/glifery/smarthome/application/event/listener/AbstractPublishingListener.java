package org.glifery.smarthome.application.event.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.glifery.smarthome.application.port.EventStoreInterface;
import org.glifery.smarthome.domain.model.event.AbstractEvent;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractPublishingListener extends AbstractListener {
    protected final EventStoreInterface eventStore;

    protected void publishAndLog(AbstractEvent event) {
        eventStore.publish(this.getListenerName(), event);
    }
}
