package org.glifery.smarthome.adapter.controller.converter;

import lombok.RequiredArgsConstructor;
import org.glifery.smarthome.application.port.ControllerRepositoryInterface;
import org.glifery.smarthome.domain.model.megad.MegaD;
import org.springframework.core.convert.converter.Converter;

@RequiredArgsConstructor
public class StringToMegadIdConverter implements Converter<String, MegaD> {
    private final ControllerRepositoryInterface controllerRepository;

    @Override
    public MegaD convert(String megadId) {
        return controllerRepository.findMegadId(megadId);
    }
}
