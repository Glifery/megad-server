package org.glifery.smarthome.application.service;

import org.glifery.smarthome.application.configuration.MegadConfig;
import org.glifery.smarthome.application.port.MegadGatewayInterface;
import org.glifery.smarthome.application.port.PortStateRepositoryInterface;
import org.glifery.smarthome.domain.model.megad.MegadId;
import org.glifery.smarthome.domain.model.megad.ActionsList;
import org.glifery.smarthome.domain.model.megad.SingleAction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;

import java.io.IOException;

@RunWith(JUnit4.class)
public class MegadServiceTest {
    private MegadGatewayInterface megadGateway;
    private MegadService megadService;

    @Captor
    private ArgumentCaptor<MegadId> megadIdArgumentCaptor;

    @Captor
    private ArgumentCaptor<ActionsList> operationArgumentCaptor;

    @Before
    public void setup() {
        MegadConfig config = Mockito.mock(MegadConfig.class);
        PortStateRepositoryInterface portStateRepository = Mockito.mock(PortStateRepositoryInterface.class);

        megadGateway = Mockito.mock(MegadGatewayInterface.class);

        megadService = new MegadService(config, megadGateway, portStateRepository);
    }

    @Test
    public void testMultipleActionsDifferentMegadId() throws IOException {
        ActionsList actionsListInput = ActionsList.create(
                SingleAction.create("megad1", 7, SingleAction.Action.ON),
                SingleAction.create("megad1", 8, SingleAction.Action.OFF),
                SingleAction.create("megad2", 9, SingleAction.Action.SWITCH)
        );

        megadService.sendCommand(actionsListInput);

//        Mockito.verify(megadGateway, Mockito.times(2)).sendCommand(megadIdArgumentCaptor.capture(), operationArgumentCaptor.capture());
        Mockito.verify(megadGateway, Mockito.times(2)).sendCommand(Mockito.any(MegadId.class), Mockito.any(ActionsList.class));

//        List<MegadId> megadIds = megadIdArgumentCaptor.getAllValues();
//        List<Operation> operations = operationArgumentCaptor.getAllValues();
//
//        Assertions.assertThat(megadIds).hasSize(2);
//        Assertions.assertThat(megadIds.get(0).toString()).isEqualTo("megad1");
//        Assertions.assertThat(megadIds.get(2).toString()).isEqualTo("megad2");
//
//        Assertions.assertThat(operations).hasSize(2);
//        Assertions.assertThat(operations.get(0).getPortActions()).hasSize(2);
//        Assertions.assertThat(operations.get(0).getPortActions().get(0).getPort()).isEqualTo(7);
//        Assertions.assertThat(operations.get(0).getPortActions().get(1).getPort()).isEqualTo(8);
//        Assertions.assertThat(operations.get(1).getPortActions()).hasSize(1);
//        Assertions.assertThat(operations.get(1).getPortActions().get(0).getPort()).isEqualTo(9);
    }

    @Test
    public void testOneMegadId() throws IOException {
        ActionsList actionsListInput = ActionsList.create(
                SingleAction.create("megad1", 7, SingleAction.Action.ON),
                SingleAction.create("megad1", 8, SingleAction.Action.OFF),
                SingleAction.create("megad1", 9, SingleAction.Action.SWITCH)
        );

        megadService.sendCommand(actionsListInput);

        Mockito.verify(megadGateway, Mockito.times(1)).sendCommand(Mockito.any(MegadId.class), Mockito.any(ActionsList.class));
    }
}
