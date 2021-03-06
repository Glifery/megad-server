package org.glifery.smarthome.adapter.controller.configuration;

import org.glifery.smarthome.adapter.controller.converter.StringToClickTypeConverter;
import org.glifery.smarthome.adapter.controller.converter.StringToModeConverter;
import org.glifery.smarthome.adapter.controller.converter.StringToStatusConverter;
import org.glifery.smarthome.adapter.controller.converter.StringToMegadIdConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToMegadIdConverter());
        registry.addConverter(new StringToModeConverter());
        registry.addConverter(new StringToStatusConverter());
        registry.addConverter(new StringToClickTypeConverter());
    }
}
