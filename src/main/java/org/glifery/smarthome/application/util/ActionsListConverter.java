package org.glifery.smarthome.application.util;

import org.glifery.smarthome.application.exception.InvalidActionException;
import org.glifery.smarthome.domain.model.megad.ActionsList;
import org.glifery.smarthome.domain.model.megad.MegadId;
import org.glifery.smarthome.domain.model.megad.Port;
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
    public static ActionsList fromActionString(MegadId megadId, String actionString) {
        List<String> singleActionStrings = splitBySingleActionStrings(filterOnlySimpleClick(actionString));

        ActionsList actionsList = new ActionsList();
        singleActionStrings.stream().forEach(singleActionString -> actionsList.getSingleActions().add(createSingleActionFromActionString(megadId, singleActionString)));

        validateActionsList(actionsList, actionString);

        return actionsList;
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

    private static SingleAction createSingleActionFromActionString(MegadId megadId, String actionString) {
        List<String> portAndAction = Arrays.stream(actionString.split(":")).collect(Collectors.toList());

        if (portAndAction.size() != 2) {
            throw new InvalidActionException(String.format("Unable to parse MegaD action '%s': no ':' delimiter found", actionString));
        }

        try {
            Integer portNumber = Integer.parseInt(portAndAction.get(0));
            Integer actionCode = Integer.parseInt(portAndAction.get(1));

            SingleAction singleAction = new SingleAction(
                    new Port(megadId, portNumber),
                    actionCode
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
