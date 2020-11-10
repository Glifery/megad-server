package org.glifery.smarthome.application.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "spreadsheet")
public class SpreadsheetConfig {
    private String url;
    private String tab;
    private String megadConfigColumn;
}
