package org.glifery.smarthome.application.util;

import org.glifery.smarthome.application.exception.InvalidActionException;
import org.glifery.smarthome.application.port.PortRepositoryInterface;
import org.glifery.smarthome.domain.model.megad.ActionsList;
import org.glifery.smarthome.domain.model.megad.MegadId;
import org.glifery.smarthome.domain.model.megad.SingleAction;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ActionsListConverter {
    public static ActionsList fromActionString(PortRepositoryInterface config, MegadId megadId, String actionString) {
        List<String> singleActionStrings = splitBySingleActionStrings(filterOnlySimpleClick(actionString));

        ActionsList actionsList = new ActionsList();
        singleActionStrings.stream().forEach(singleActionString -> actionsList.getSingleActions().add(createSingleActionFromActionString(config, megadId, singleActionString)));

        validateActionsList(actionsList, actionString);

        return actionsList;
    }

    public static String toMegadCommand(ActionsList actionsList) {
        return actionsList.getSingleActions().stream()
                .map(ActionsListConverter::toMegadCommand)
                .collect(Collectors.joining(";"));
    }

    private static String toMegadCommand(SingleAction singleAction) {
        Integer actionNumber = SingleAction.actionMap.entrySet().stream()
                .filter(integerActionEntry -> singleAction.getAction().equals(integerActionEntry.getValue()))
                .findFirst()
                .map(integerActionEntry -> integerActionEntry.getKey())
                .orElseThrow(() -> new InvalidActionException(String.format("Action %s is not implemented", singleAction)));

        return String.format("%s:%s", singleAction.getPort().getNumber(), actionNumber);
    }

    private static String filterOnlySimpleClick(String actionString) {
        if (actionString.contains("|")) {
            return Arrays.stream(actionString.split("\\|")).collect(Collectors.toList()).get(0);
        }

        return actionString;
    }

    private static List<String> splitBySingleActionStrings(String actionString) {
        if (actionString.contains(";")) {
            return Arrays.stream(actionString.split(";")).collect(Collectors.toList());
        }

        return new ArrayList<String>(){{
            add(actionString);
        }};
    }

    private static SingleAction createSingleActionFromActionString(PortRepositoryInterface config, MegadId megadId, String actionString) {
        List<String> portAndAction = Arrays.stream(actionString.split(":")).collect(Collectors.toList());

        if (portAndAction.size() != 2) {
            throw new InvalidActionException(String.format("Unable to parse MegaD action '%s': no ':' delimiter found", actionString));
        }

        try {
            Integer portNumber = Integer.parseInt(portAndAction.get(0));
            Integer actionCode = Integer.parseInt(portAndAction.get(1));

            if (!SingleAction.actionMap.containsKey(actionCode)) {
                throw new InvalidActionException(String.format("Unable to parse MegaD action '%s': action is not supported", actionString));
            }

            SingleAction singleAction = new SingleAction(
                    config.findPort(megadId, portNumber),
                    SingleAction.actionMap.get(actionCode)
            );

            return singleAction;
        } catch (NumberFormatException e) {
            throw new InvalidActionException(String.format("Unable to parse MegaD action '%s': port or action is not a number", actionString));
        }
    }

    private static void validateActionsList(ActionsList actionsList, String actionString) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<ActionsList>> violations = validator.validate(actionsList);
        if (!violations.isEmpty()) {
            throw new InvalidActionException(String.format("Unable to parse MegaD action '%s': validation errors: %s", actionString, violations.toString()));
        }
    }
}
