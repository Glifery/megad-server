package org.glifery.smarthome.adapter.google.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.glifery.smarthome.domain.model.megad.MegadId;

@Getter
@RequiredArgsConstructor
public class MegadRange {
    private final MegadId megadId;
    private final String range;
}
