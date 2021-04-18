package org.glifery.smarthome.application.configuration;

import lombok.Data;
import org.glifery.smarthome.application.exception.InvalidPortException;
import org.glifery.smarthome.application.port.ControllerRepositoryInterface;
import org.glifery.smarthome.application.port.PortRepositoryInterface;
import org.glifery.smarthome.domain.model.megad.MegadId;
import org.glifery.smarthome.domain.model.megad.Port;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Configuration
@ConfigurationProperties(prefix = "megad")
public class MegadConfig implements ControllerRepositoryInterface, PortRepositoryInterface {
    private CommonConfig common;
    private Map<String, ControllerConfig> controllers;
    private List<MegadId> megadIds;
    private List<Port> ports;

    public void setCommon(CommonConfig common) {
        this.common = common;
    }

    public void setControllers(Map<String, ControllerConfig> controllers) {
        this.controllers = controllers;

        this.megadIds = new ArrayList<>();
        this.ports = controllers.entrySet().stream()
                .flatMap(stringControllerConfigEntry -> {
                    MegadId megadId = new MegadId(stringControllerConfigEntry.getKey());

                    megadIds.add(megadId);

                    return stringControllerConfigEntry.getValue().getTitles()
                            .entrySet().stream()
                            .map(integerStringEntry -> new Port(megadId, integerStringEntry.getKey(), integerStringEntry.getValue()));
                })
                .collect(Collectors.toList());
    }

    @Override
    public Port findPort(MegadId megadId, Integer portNumber) {
        return ports.stream()
                .filter(port -> port.getMegadId().equals(megadId) && port.getNumber().equals(portNumber))
                .findFirst()
                .orElseThrow(() -> new InvalidPortException(String.format("Unable to find port %s.%s", megadId, portNumber)));
    }

    @Override
    public MegadId findMegadId(String megadIdName) {
        return megadIds.stream()
                .filter(megadId -> megadId.toString().equals(megadIdName))
                .findFirst()
                .orElseThrow(() -> new InvalidPortException(String.format("Unable to find megad %s", megadIdName)));
    }

    @Data
    public static class CommonConfig {
        private String password;
    }

    @Data
    public static class ControllerConfig {
        private String host;
        private String name;
        private SpreadsheetConfig spreadsheet;
        private Map<Integer, String> titles;
    }

    @Data
    public static class SpreadsheetConfig {
        private Integer firstRowForP0;
    }
}
