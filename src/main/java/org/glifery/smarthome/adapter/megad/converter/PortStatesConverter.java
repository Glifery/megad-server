package org.glifery.smarthome.adapter.megad.converter;

import lombok.extern.slf4j.Slf4j;
import org.glifery.smarthome.application.exception.InvalidPortException;
import org.glifery.smarthome.application.port.PortRepositoryInterface;
import org.glifery.smarthome.domain.model.megad.MegaD;
import org.glifery.smarthome.domain.model.megad.Port;
import org.glifery.smarthome.domain.model.megad.PortState;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PortStatesConverter {
    private static String DELIMETER_PORT = ";";
    private static String DELIMETER_STATE = "/";

    public static List<PortState> convert(PortRepositoryInterface portRepository, MegaD megaD, String allStatesResponse) {
        List<String> portParts = Arrays.stream(allStatesResponse.split(DELIMETER_PORT)).collect(Collectors.toList());
        List<PortState.State> portStateList = portParts.stream()
                .map(PortStatesConverter::retrieveStateString)
                .map(PortStatesConverter::convertStateToEnum)
                .collect(Collectors.toList());

        return IntStream.range(0, 28)
                .mapToObj(i -> convertToPortState(portRepository, megaD, i, portStateList.get(i)))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private static String retrieveStateString(String portString) {
        return Arrays.stream(portString.split(DELIMETER_STATE))
                .collect(Collectors.toList())
                .get(0);
    }

    private static PortState.State convertStateToEnum(String stateString) {
        switch (stateString) {
            case "ON": return PortState.State.ON;
            case "OFF": return PortState.State.OFF;
            default: return PortState.State.OFF;
        }
    }

    private static PortState convertToPortState(PortRepositoryInterface portRepository, MegaD megaD, Integer portNumber, PortState.State state) {
        try {
            Port port = portRepository.findPort(megaD, portNumber);

            return PortState.create(port, state);
        } catch (InvalidPortException e) {
            return null;
        }
    }
}
