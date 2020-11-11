package org.glifery.smarthome.adapter.controller.converter;

import org.glifery.smarthome.domain.model.megad.MegadId;
import org.springframework.core.convert.converter.Converter;

public class StringToMegadIdConverter implements Converter<String, MegadId> {
    @Override
    public MegadId convert(String megadId) {
        return new MegadId(megadId);
    }
}
