package org.glifery.smarthome.adapter.controller.converter;

import org.glifery.smarthome.domain.model.megad.ActionIncomingRequest;
import org.springframework.core.convert.converter.Converter;

public class StringToClickTypeConverter implements Converter<String, ActionIncomingRequest.ClickType> {
    @Override
    public ActionIncomingRequest.ClickType convert(String clickType) {
        switch (clickType) {
            case "1": return ActionIncomingRequest.ClickType.SINGLE;
            case "2": return ActionIncomingRequest.ClickType.DOUBLE;
            default: return null;
        }
    }
}
