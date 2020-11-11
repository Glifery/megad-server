package org.glifery.smarthome.application.util;

import org.assertj.core.api.Assertions;
import org.glifery.smarthome.application.exception.InvalidActionException;
import org.glifery.smarthome.domain.model.megad.ActionsList;
import org.glifery.smarthome.domain.model.megad.MegadId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;

@RunWith(JUnit4.class)
public class ActionListConverterTest {
    @Test
    public void testSingleAction() throws IOException {
        String actionString = "7:0";

        ActionsList actionsList = ActionsListConverter.fromActionString(new MegadId("megad1"), actionString);

        Assertions.assertThat(actionsList.getSingleActions()).hasSize(1);
        Assertions.assertThat(actionsList.getSingleActions().get(0).getPort().getMegadId().toString()).isEqualTo("megad1");
        Assertions.assertThat(actionsList.getSingleActions().get(0).getPort().toString()).isEqualTo("7");
        Assertions.assertThat(actionsList.getSingleActions().get(0).toString()).isEqualTo("7:0");
    }

    @Test
    public void testMultipleActions() throws IOException {
        String actionString = "7:1;8:2;9:3";

        ActionsList actionsList = ActionsListConverter.fromActionString(new MegadId("megad1"), actionString);

        Assertions.assertThat(actionsList.getSingleActions()).hasSize(3);
        Assertions.assertThat(actionsList.getSingleActions().get(0).getPort().getMegadId().toString()).isEqualTo("megad1");
        Assertions.assertThat(actionsList.getSingleActions().get(0).getPort().toString()).isEqualTo("7");
        Assertions.assertThat(actionsList.getSingleActions().get(0).toString()).isEqualTo("7:1");
        Assertions.assertThat(actionsList.getSingleActions().get(1).getPort().getMegadId().toString()).isEqualTo("megad1");
        Assertions.assertThat(actionsList.getSingleActions().get(1).getPort().toString()).isEqualTo("8");
        Assertions.assertThat(actionsList.getSingleActions().get(1).toString()).isEqualTo("8:2");
        Assertions.assertThat(actionsList.getSingleActions().get(2).getPort().getMegadId().toString()).isEqualTo("megad1");
        Assertions.assertThat(actionsList.getSingleActions().get(2).getPort().toString()).isEqualTo("9");
        Assertions.assertThat(actionsList.getSingleActions().get(2).toString()).isEqualTo("9:3");
    }

    @Test
    public void testMultipleActionsWithBar() throws IOException {
        String actionString = "7:1;8:2|9:3";

        ActionsList actionsList = ActionsListConverter.fromActionString(new MegadId("megad1"), actionString);

        Assertions.assertThat(actionsList.getSingleActions()).hasSize(2);
        Assertions.assertThat(actionsList.getSingleActions().get(0).getPort().getMegadId().toString()).isEqualTo("megad1");
        Assertions.assertThat(actionsList.getSingleActions().get(0).getPort().toString()).isEqualTo("7");
        Assertions.assertThat(actionsList.getSingleActions().get(0).toString()).isEqualTo("7:1");
        Assertions.assertThat(actionsList.getSingleActions().get(1).getPort().getMegadId().toString()).isEqualTo("megad1");
        Assertions.assertThat(actionsList.getSingleActions().get(1).getPort().toString()).isEqualTo("8");
        Assertions.assertThat(actionsList.getSingleActions().get(1).toString()).isEqualTo("8:2");
    }

    @Test(expected = InvalidActionException.class)
    public void testValidationErrorWrongPort() throws IOException {
        String actionString = "7:1;8888:2";

        ActionsListConverter.fromActionString(new MegadId("megad1"), actionString);
    }

    @Test(expected = InvalidActionException.class)
    public void testValidationErrorWrongAction() throws IOException {
        String actionString = "7:1;8:8";

        ActionsListConverter.fromActionString(new MegadId("megad1"), actionString);
    }

    @Test(expected = InvalidActionException.class)
    public void testValidationErrorEmptyAction() throws IOException {
        String actionString = "";

        ActionsListConverter.fromActionString(new MegadId("megad1"), actionString);
    }
}
