package org.glifery.smarthome.application.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "megad")
public class MegadConfig {
    private CommonConfig common;
    private Map<String, ControllerConfig> controllers;

    @Data
    public static class CommonConfig {
        private String password;
    }

    @Data
    public static class ControllerConfig {
        private String host;
        private String name;
        private SpreadsheetConfig spreadsheet;
    }

    @Data
    public static class SpreadsheetConfig {
        private Integer firstRowForP0;
    }
}
