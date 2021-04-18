package org.glifery.smarthome.application.configuration;

import lombok.Data;
import org.glifery.smarthome.application.exception.InvalidPortException;
import org.glifery.smarthome.application.port.ControllerRepositoryInterface;
import org.glifery.smarthome.application.port.PortRepositoryInterface;
import org.glifery.smarthome.domain.model.megad.MegaD;
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
    private List<MegaD> megaDs;
    private List<Port> ports;

    public void setCommon(CommonConfig common) {
        this.common = common;
    }

    public void setControllers(Map<String, ControllerConfig> controllers) {
        this.controllers = controllers;

        this.megaDs = new ArrayList<>();
        this.ports = controllers.entrySet().stream()
                .flatMap(stringControllerConfigEntry -> {
                    MegaD megaD = new MegaD(stringControllerConfigEntry.getKey());

                    megaDs.add(megaD);

                    return stringControllerConfigEntry.getValue().getPorts()
                            .entrySet().stream()
                            .map(integerStringEntry -> new Port(
                                    megaD, integerStringEntry.getKey(),
                                    integerStringEntry.getValue().getTitle(),
                                    integerStringEntry.getValue().getType()
                            ));
                })
                .collect(Collectors.toList());
    }

    @Override
    public Port findPort(MegaD megaD, Integer portNumber) {
        return ports.stream()
                .filter(port -> port.getMegaD().equals(megaD) && port.getNumber().equals(portNumber))
                .findFirst()
                .orElseThrow(() -> new InvalidPortException(String.format("Unable to find port %s.%s", megaD, portNumber)));
    }

    @Override
    public List<Port> findAll() {
        return ports;
    }

    @Override
    public MegaD findMegadId(String megadIdName) {
        return megaDs.stream()
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
        private Map<Integer, PortConfig> ports;
    }

    @Data
    public static class SpreadsheetConfig {
        private Integer firstRowForP0;
    }

    @Data
    public static class PortConfig {
        private String title;
        private Port.Type type;
    }
}
