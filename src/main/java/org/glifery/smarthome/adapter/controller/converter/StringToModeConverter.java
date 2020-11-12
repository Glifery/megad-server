package org.glifery.smarthome.adapter.controller.converter;

import org.glifery.smarthome.domain.model.megad.ActionIncomingRequest;
import org.springframework.core.convert.converter.Converter;

public class StringToModeConverter implements Converter<String, ActionIncomingRequest.Mode> {
    @Override
    public ActionIncomingRequest.Mode convert(String mode) {
        switch (mode) {
            case "1": return ActionIncomingRequest.Mode.RELEASE;
            case "2": return ActionIncomingRequest.Mode.HOLD;
            default: return null;
        }
    }
}
