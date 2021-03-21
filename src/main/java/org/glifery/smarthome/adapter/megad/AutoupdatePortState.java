package org.glifery.smarthome.adapter.megad;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.glifery.smarthome.application.configuration.ApplicationConfig;
import org.glifery.smarthome.application.port.AutoupdatePortStateInterface;
import org.glifery.smarthome.application.service.MegadService;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class AutoupdatePortState implements AutoupdatePortStateInterface {
    private static Integer DELAY_TIME_SECONDS = 0;

    private final ApplicationConfig config;
    private final MegadService megadService;

    @Override
    public void init() {
        log.info("Initialize auto-updating of port states");
        Runnable autoupdatePortStateTask = () -> {
            megadService.updateAllStates();
        };

        ScheduledExecutorService autoupdatePortStateExecutor = Executors.newSingleThreadScheduledExecutor();
        autoupdatePortStateExecutor.scheduleAtFixedRate(
                autoupdatePortStateTask,
                DELAY_TIME_SECONDS,
                config.getAutoupdatePortStateSeconds(),
                TimeUnit.SECONDS
        );
    }
}
