package org.glifery.smarthome.adapter.controller.converter;

import lombok.RequiredArgsConstructor;
import org.glifery.smarthome.application.port.ControllerRepositoryInterface;
import org.glifery.smarthome.domain.model.megad.MegadId;
import org.springframework.core.convert.converter.Converter;

@RequiredArgsConstructor
public class StringToMegadIdConverter implements Converter<String, MegadId> {
    private final ControllerRepositoryInterface controllerRepository;

    @Override
    public MegadId convert(String megadId) {
        return controllerRepository.findMegadId(megadId);
    }
}
