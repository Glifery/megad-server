package org.glifery.smarthome.adapter.megad.converter;

import org.glifery.smarthome.domain.model.megad.MegadId;
import org.glifery.smarthome.domain.model.megad.Port;
import org.glifery.smarthome.domain.model.megad.PortState;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PortStatesConverter {
    private static String DELIMETER_PORT = ";";
    private static String DELIMETER_STATE = "/";

    public static List<PortState> convert(MegadId megadId, String allStatesResponse) {
        List<String> portParts = Arrays.stream(allStatesResponse.split(DELIMETER_PORT)).collect(Collectors.toList());
        List<PortState.State> portStateList = portParts.stream()
                .map(PortStatesConverter::retrieveStateString)
                .map(PortStatesConverter::convertStateToEnum)
                .collect(Collectors.toList());

        return IntStream.range(0, 28)
                .mapToObj(i -> convertToPortState(megadId, i, portStateList.get(i)))
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

    private static PortState convertToPortState(MegadId megadId, Integer port, PortState.State state) {
        return PortState.create(
                Port.create(megadId.toString(), port),
                state
        );
    }
}
