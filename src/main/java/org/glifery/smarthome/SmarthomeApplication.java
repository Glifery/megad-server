package org.glifery.smarthome;

import lombok.RequiredArgsConstructor;
import org.glifery.smarthome.application.configuration.ApplicationConfig;
import org.glifery.smarthome.application.port.AutoupdatePortStateInterface;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class SmarthomeApplication implements ApplicationRunner {
	private final ApplicationConfig config;
	private final AutoupdatePortStateInterface autoupdatePortState;

	public static void main(String[] args) {
		SpringApplication.run(SmarthomeApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) {
		if (!config.isAutoupdatePortStateDisabled()) {
			autoupdatePortState.init();
		}
	}
}
