package org.glifery.smarthome.adapter.google.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.glifery.smarthome.domain.model.megad.MegaD;

@Getter
@RequiredArgsConstructor
public class MegadRange {
    private final MegaD megaD;
    private final String range;
}
