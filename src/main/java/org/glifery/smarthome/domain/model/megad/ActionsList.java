package org.glifery.smarthome.domain.model.megad;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class ActionsList {
    @Valid
    @Getter
    private List<SingleAction> singleActions = new ArrayList<>();

    public static ActionsList create(SingleAction... singleActions) {
        ActionsList actionsList = new ActionsList();

        for (SingleAction singleAction : singleActions) {
            actionsList.getSingleActions().add(singleAction);
        }

        return actionsList;
    }

    public String toString() {
        List<String> portActionsString = singleActions.stream().map(SingleAction::toString).collect(Collectors.toList());

        return String.join(";", portActionsString);
    }
}
