package org.glifery.smarthome.adapter.controller.converter;

import org.glifery.smarthome.domain.model.megad.ActionIncomingRequest;
import org.springframework.core.convert.converter.Converter;

public class StringToClickTypeConverter implements Converter<String, ActionIncomingRequest.Type> {
    @Override
    public ActionIncomingRequest.Type convert(String clickType) {
        switch (clickType) {
            case "1": return ActionIncomingRequest.Type.SINGLE;
            case "2": return ActionIncomingRequest.Type.DOUBLE;
            default: return null;
        }
    }
}
