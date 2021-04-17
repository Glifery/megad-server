package org.glifery.smarthome.adapter.memory;

import org.assertj.core.api.Assertions;
import org.glifery.smarthome.domain.model.event.AbstractEvent;
import org.glifery.smarthome.domain.model.event.ActionIncomingRequestEvent;
import org.glifery.smarthome.domain.model.event.ClickEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;

@RunWith(JUnit4.class)
public class InMemoryGroupedEventRepositoryTest {
    private ApplicationEventPublisher publisher;
    private InMemoryGroupedEventRepository repository;
    private ActionIncomingRequestEvent actionIncomingRequestEvent;

    @Before
    public void setup() {
        publisher = Mockito.mock(ApplicationEventPublisher.class);
        repository = new InMemoryGroupedEventRepository(publisher);
        actionIncomingRequestEvent = Mockito.mock(ActionIncomingRequestEvent.class);
    }

    @Test
    public void testEmptyOnStart() {
        Assertions.assertThat(repository.findLatestByName("eventName").orElse(null)).isNull();
        Assertions.assertThat(repository.findByNameDesc("eventName", 10)).hasSize(0);
    }

    @Test
    public void testReturnOneFromOne() {
        AbstractEvent event = new ClickEvent("eventName", LocalDateTime.now(), actionIncomingRequestEvent);

        repository.publish(event);

        Assertions.assertThat(repository.findLatestByName("eventName").orElse(null)).isEqualTo(event);
        Assertions.assertThat(repository.findLatestByName("anotherName").orElse(null)).isNull();
        Assertions.assertThat(repository.findByNameDesc("eventName", 10)).hasSize(1);
        Assertions.assertThat(repository.findByNameDesc("eventName", 10)).contains(event);
        Assertions.assertThat(repository.findByNameDesc("anotherName", 10)).hasSize(0);
    }

    @Test
    public void testReturnThreeFromFive() {
        AbstractEvent event1 = new ClickEvent("eventName", LocalDateTime.now(), actionIncomingRequestEvent);
        AbstractEvent event2 = new ClickEvent("eventName", LocalDateTime.now(), actionIncomingRequestEvent);
        AbstractEvent event3 = new ClickEvent("eventName", LocalDateTime.now(), actionIncomingRequestEvent);
        AbstractEvent event4 = new ClickEvent("eventName", LocalDateTime.now(), actionIncomingRequestEvent);
        AbstractEvent event5 = new ClickEvent("eventName", LocalDateTime.now(), actionIncomingRequestEvent);

        repository.publish(event1);
        repository.publish(event2);
        repository.publish(event3);
        repository.publish(event4);
        repository.publish(event5);

        Assertions.assertThat(repository.findLatestByName("eventName").orElse(null)).isEqualTo(event5);
        Assertions.assertThat(repository.findByNameDesc("eventName", 3)).hasSize(3);

        Assertions.assertThat(repository.findByNameDesc("eventName", 3).get(0)).isEqualTo(event5);
        Assertions.assertThat(repository.findByNameDesc("eventName", 3).get(1)).isEqualTo(event4);
        Assertions.assertThat(repository.findByNameDesc("eventName", 3).get(2)).isEqualTo(event3);

        Assertions.assertThat(repository.findByNameDesc("eventName", 10)).hasSize(5);
    }


    @Test
    public void testReturnThreeAfterClear() {
        AbstractEvent event1 = new ClickEvent("eventName", LocalDateTime.now().minusDays(3), actionIncomingRequestEvent);
        AbstractEvent event2 = new ClickEvent("eventName", LocalDateTime.now().minusHours(25), actionIncomingRequestEvent);
        AbstractEvent event3 = new ClickEvent("eventName", LocalDateTime.now().minusHours(12), actionIncomingRequestEvent);
        AbstractEvent event4 = new ClickEvent("eventName", LocalDateTime.now().minusDays(3), actionIncomingRequestEvent);
        AbstractEvent event5 = new ClickEvent("eventName", LocalDateTime.now().minusHours(1), actionIncomingRequestEvent);

        repository.publish(event1);
        repository.publish(event2);
        repository.publish(event3);
        repository.publish(event4);
        repository.publish(event5);

        Assertions.assertThat(repository.findLatestByName("eventName").orElse(null)).isEqualTo(event5);
        Assertions.assertThat(repository.findByNameDesc("eventName", 10)).hasSize(5);
        Assertions.assertThat(repository.findByNameDesc("eventName", 10).get(0)).isEqualTo(event5);

        repository.clearByTtl();

        Assertions.assertThat(repository.findLatestByName("eventName").orElse(null)).isEqualTo(event5);
        Assertions.assertThat(repository.findByNameDesc("eventName", 10)).hasSize(3);
        Assertions.assertThat(repository.findByNameDesc("eventName", 10).get(0)).isEqualTo(event5);
        Assertions.assertThat(repository.findByNameDesc("eventName", 10).get(1)).isEqualTo(event4);
        Assertions.assertThat(repository.findByNameDesc("eventName", 10).get(2)).isEqualTo(event3);

        repository.clearByTtl();

        Assertions.assertThat(repository.findByNameDesc("eventName", 10)).hasSize(3);
        Assertions.assertThat(repository.findByNameDesc("eventName", 10).get(0)).isEqualTo(event5);
    }
}
