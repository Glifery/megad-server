package org.glifery.smarthome.application.service;

import org.glifery.smarthome.application.configuration.MegadConfig;
import org.glifery.smarthome.application.port.ControllerRepositoryInterface;
import org.glifery.smarthome.application.port.MegadGatewayInterface;
import org.glifery.smarthome.application.port.PortRepositoryInterface;
import org.glifery.smarthome.application.port.PortStateRepositoryInterface;
import org.glifery.smarthome.domain.model.megad.ActionsList;
import org.glifery.smarthome.domain.model.megad.MegadId;
import org.glifery.smarthome.domain.model.megad.Port;
import org.glifery.smarthome.domain.model.megad.SingleAction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import java.io.IOException;

@RunWith(JUnit4.class)
public class MegadServiceTest {
    private MegadGatewayInterface megadGateway;
    private MegadService megadService;
    private ControllerRepositoryInterface controllerRepository;
    private PortRepositoryInterface portRepository;

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
    public void setup() {
        MegadConfig config = Mockito.mock(MegadConfig.class);
        controllerRepository = new MegadIdRepository();
        portRepository = new PortRepository();
        PortStateRepositoryInterface portStateRepository = Mockito.mock(PortStateRepositoryInterface.class);

        megadGateway = Mockito.mock(MegadGatewayInterface.class);

        megadService = new MegadService(config, controllerRepository, megadGateway, portStateRepository);
    }

    @Test
    public void testMultipleActionsDifferentMegadId() throws IOException {
        ActionsList actionsListInput = ActionsList.create(
                createSingleAction("megad1", 7, SingleAction.Action.ON),
                createSingleAction("megad1", 8, SingleAction.Action.OFF),
                createSingleAction("megad2", 9, SingleAction.Action.SWITCH)
        );

        megadService.sendCommand(actionsListInput);

        Mockito.verify(megadGateway, Mockito.times(2)).sendCommand(Mockito.any(MegadId.class), Mockito.any(ActionsList.class));
    }

    @Test
    public void testOneMegadId() throws IOException {
        ActionsList actionsListInput = ActionsList.create(
                createSingleAction("megad1", 7, SingleAction.Action.ON),
                createSingleAction("megad1", 8, SingleAction.Action.OFF),
                createSingleAction("megad1", 9, SingleAction.Action.SWITCH)
        );

        megadService.sendCommand(actionsListInput);

        Mockito.verify(megadGateway, Mockito.times(1)).sendCommand(Mockito.any(MegadId.class), Mockito.any(ActionsList.class));
    }

    private SingleAction createSingleAction(String megadId, Integer port, SingleAction.Action action) {
        return new SingleAction(
                portRepository.findPort(
                        controllerRepository.findMegadId(megadId),
                        port
                ),
                action
        );
    }
}
