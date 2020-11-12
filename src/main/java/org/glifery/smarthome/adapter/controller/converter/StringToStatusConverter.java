package org.glifery.smarthome.adapter.controller.converter;

import org.glifery.smarthome.domain.model.megad.StatusIncomingRequest;
import org.springframework.core.convert.converter.Converter;

public class StringToStatusConverter implements Converter<String, StatusIncomingRequest.Status> {
    @Override
    public StatusIncomingRequest.Status convert(String status) {
        switch (status) {
            case "1": return StatusIncomingRequest.Status.ON;
            case "0": return StatusIncomingRequest.Status.OFF;
            default: return null;
        }
    }
}
