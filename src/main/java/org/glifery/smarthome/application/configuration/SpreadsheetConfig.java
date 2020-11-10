package org.glifery.smarthome.application.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "spreadsheet")
public class SpreadsheetConfig {
    private String credentialsJson;
    private String tokensFolder;
    private String spreadsheetId;
    private String spreadsheetTab;
    private String tab;
    private String megadConfigColumn;
}
