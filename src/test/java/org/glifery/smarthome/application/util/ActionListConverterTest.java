package org.glifery.smarthome.application.util;

import org.assertj.core.api.Assertions;
import org.glifery.smarthome.application.exception.InvalidActionException;
import org.glifery.smarthome.application.port.ControllerRepositoryInterface;
import org.glifery.smarthome.application.port.PortRepositoryInterface;
import org.glifery.smarthome.domain.model.megad.ActionsList;
import org.glifery.smarthome.domain.model.megad.MegadId;
import org.glifery.smarthome.domain.model.megad.Port;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;

@RunWith(JUnit4.class)
public class ActionListConverterTest {
    private PortRepositoryInterface portRepository;
    private ControllerRepositoryInterface controllerRepository;

    class MegadIdRepository implements ControllerRepositoryInterface {
        @Override
        public MegadId findMegadId(String megadId) {
            return new MegadId(megadId);
        }
    }

    class PortRepository implements PortRepositoryInterface {
        @Override
        public Port findPort(MegadId megadId, Integer port) {
            return new Port(megadId, port, String.format("%s.%s", megadId, port));
        }
    }

    @Before
    public void setUp() {
        controllerRepository = new MegadIdRepository();
        portRepository = new PortRepository();
    }

    @Test
    public void testSingleAction() throws IOException {
        String actionString = "7:0";

        ActionsList actionsList = ActionsListConverter.fromActionString(portRepository, controllerRepository.findMegadId("megad1"), actionString);

        Assertions.assertThat(actionsList.getSingleActions()).hasSize(1);
        Assertions.assertThat(actionsList.getSingleActions().get(0).getPort().getMegadId().toString()).isEqualTo("megad1");
        Assertions.assertThat(actionsList.getSingleActions().get(0).getPort().getNumber()).isEqualTo(7);
        Assertions.assertThat(actionsList.getSingleActions().get(0).toString()).isEqualTo("megad1.7:OFF");
    }

    @Test
    public void testMultipleActions() throws IOException {
        String actionString = "7:1;8:2;9:3";

        ActionsList actionsList = ActionsListConverter.fromActionString(portRepository, controllerRepository.findMegadId("megad1"), actionString);

        Assertions.assertThat(actionsList.getSingleActions()).hasSize(3);
        Assertions.assertThat(actionsList.getSingleActions().get(0).getPort().getMegadId().toString()).isEqualTo("megad1");
        Assertions.assertThat(actionsList.getSingleActions().get(0).getPort().getNumber()).isEqualTo(7);
        Assertions.assertThat(actionsList.getSingleActions().get(0).toString()).isEqualTo("megad1.7:ON");
        Assertions.assertThat(actionsList.getSingleActions().get(1).getPort().getMegadId().toString()).isEqualTo("megad1");
        Assertions.assertThat(actionsList.getSingleActions().get(1).getPort().getNumber()).isEqualTo(8);
        Assertions.assertThat(actionsList.getSingleActions().get(1).toString()).isEqualTo("megad1.8:SWITCH");
        Assertions.assertThat(actionsList.getSingleActions().get(2).getPort().getMegadId().toString()).isEqualTo("megad1");
        Assertions.assertThat(actionsList.getSingleActions().get(2).getPort().getNumber()).isEqualTo(9);
        Assertions.assertThat(actionsList.getSingleActions().get(2).toString()).isEqualTo("megad1.9:SYNC");
    }

    @Test
    public void testMultipleActionsWithBar() throws IOException {
        String actionString = "7:1;8:2|9:3";

        ActionsList actionsList = ActionsListConverter.fromActionString(portRepository, controllerRepository.findMegadId("megad1"), actionString);

        Assertions.assertThat(actionsList.getSingleActions()).hasSize(2);
        Assertions.assertThat(actionsList.getSingleActions().get(0).getPort().getMegadId().toString()).isEqualTo("megad1");
        Assertions.assertThat(actionsList.getSingleActions().get(0).getPort().getNumber()).isEqualTo(7);
        Assertions.assertThat(actionsList.getSingleActions().get(0).toString()).isEqualTo("megad1.7:ON");
        Assertions.assertThat(actionsList.getSingleActions().get(1).getPort().getMegadId().toString()).isEqualTo("megad1");
        Assertions.assertThat(actionsList.getSingleActions().get(1).getPort().getNumber()).isEqualTo(8);
        Assertions.assertThat(actionsList.getSingleActions().get(1).toString()).isEqualTo("megad1.8:SWITCH");
    }

    @Test(expected = InvalidActionException.class)
    public void testValidationErrorWrongPort() throws IOException {
        String actionString = "7:1;8888:2";

        ActionsListConverter.fromActionString(portRepository, controllerRepository.findMegadId("megad1"), actionString);
    }

    @Test(expected = InvalidActionException.class)
    public void testValidationErrorWrongAction() throws IOException {
        String actionString = "7:1;8:8";

        ActionsListConverter.fromActionString(portRepository, controllerRepository.findMegadId("megad1"), actionString);
    }

    @Test(expected = InvalidActionException.class)
    public void testValidationErrorEmptyAction() throws IOException {
        String actionString = "";

        ActionsListConverter.fromActionString(portRepository, controllerRepository.findMegadId("megad1"), actionString);
    }
}
