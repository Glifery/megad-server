package org.glifery.smarthome.adapter.controller.util;

import org.glifery.smarthome.domain.model.megad.InputAction;
import org.glifery.smarthome.domain.model.megad.MegadId;
import org.glifery.smarthome.domain.model.megad.Port;

import java.util.Optional;

public class MegadInputActionConverter {
    public static InputAction createFromServerRequest(MegadId megadId, Integer port, Integer modeInt, Integer clickIndex) {
        InputAction.Mode mode = Optional.ofNullable(modeInt).map(modeInteger -> {
            switch (modeInteger) {
                case 1: return InputAction.Mode.RELEASE;
                case 2: return InputAction.Mode.HOLD;
                default: return InputAction.Mode.PRESS;
            }
        }).orElse(InputAction.Mode.PRESS);

        clickIndex = Optional.ofNullable(clickIndex).orElse(1);

        return new InputAction(new Port(megadId, port), mode, clickIndex);
    }
}
