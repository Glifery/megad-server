package org.glifery.smarthome.application.event;

import lombok.RequiredArgsConstructor;
import org.glifery.smarthome.application.event.listener.AbstractListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EventListenerManager {
    private final List<AbstractListener> listeners;
}
