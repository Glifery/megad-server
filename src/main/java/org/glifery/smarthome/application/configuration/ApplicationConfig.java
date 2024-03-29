package org.glifery.smarthome.application.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "application")
public class ApplicationConfig {
    private boolean directMegadResponse;
    private long doubleClickMilliseconds;
    private long holdClickMilliseconds;
    private boolean autoupdatePortStateDisabled;
    private long autoupdatePortStateSeconds;
}
