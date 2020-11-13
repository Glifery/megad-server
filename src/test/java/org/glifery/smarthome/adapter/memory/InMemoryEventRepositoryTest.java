package org.glifery.smarthome.adapter.memory;

import org.assertj.core.api.Assertions;
import org.glifery.smarthome.domain.event.AbstractEvent;
import org.glifery.smarthome.domain.event.ClickEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.time.LocalDateTime;

@RunWith(JUnit4.class)
public class InMemoryEventRepositoryTest {
    private InMemoryEventRepository repository;

    @Before
    public void setup() {
        repository = new InMemoryEventRepository();
    }

    @Test
    public void testEmptyOnStart() {
        Assertions.assertThat(repository.findLatestByName("eventName")).isNull();
        Assertions.assertThat(repository.findByNameDesc("eventName", 10)).hasSize(0);
    }

    @Test
    public void testReturnOneFromOne() {
        AbstractEvent event = new ClickEvent("eventName", LocalDateTime.now());

        repository.add(event);

        Assertions.assertThat(repository.findLatestByName("eventName")).isEqualTo(event);
        Assertions.assertThat(repository.findLatestByName("anotherName")).isNull();
        Assertions.assertThat(repository.findByNameDesc("eventName", 10)).hasSize(1);
        Assertions.assertThat(repository.findByNameDesc("eventName", 10)).contains(event);
        Assertions.assertThat(repository.findByNameDesc("anotherName", 10)).hasSize(0);
    }

    @Test
    public void testReturnThreeFromFive() {
        AbstractEvent event1 = new ClickEvent("eventName", LocalDateTime.now());
        AbstractEvent event2 = new ClickEvent("eventName", LocalDateTime.now());
        AbstractEvent event3 = new ClickEvent("eventName", LocalDateTime.now());
        AbstractEvent event4 = new ClickEvent("eventName", LocalDateTime.now());
        AbstractEvent event5 = new ClickEvent("eventName", LocalDateTime.now());

        repository.add(event1);
        repository.add(event2);
        repository.add(event3);
        repository.add(event4);
        repository.add(event5);

        Assertions.assertThat(repository.findLatestByName("eventName")).isEqualTo(event5);
        Assertions.assertThat(repository.findByNameDesc("eventName", 3)).hasSize(3);

        Assertions.assertThat(repository.findByNameDesc("eventName", 3).get(0)).isEqualTo(event5);
        Assertions.assertThat(repository.findByNameDesc("eventName", 3).get(1)).isEqualTo(event4);
        Assertions.assertThat(repository.findByNameDesc("eventName", 3).get(2)).isEqualTo(event3);

        Assertions.assertThat(repository.findByNameDesc("eventName", 10)).hasSize(5);
    }


    @Test
    public void testReturnThreeAfterClear() {
        AbstractEvent event1 = new ClickEvent("eventName", LocalDateTime.now().minusDays(3));
        AbstractEvent event2 = new ClickEvent("eventName", LocalDateTime.now().minusHours(25));
        AbstractEvent event3 = new ClickEvent("eventName", LocalDateTime.now().minusHours(12));
        AbstractEvent event4 = new ClickEvent("eventName", LocalDateTime.now().minusDays(3));
        AbstractEvent event5 = new ClickEvent("eventName", LocalDateTime.now().minusHours(1));

        repository.add(event1);
        repository.add(event2);
        repository.add(event3);
        repository.add(event4);
        repository.add(event5);

        Assertions.assertThat(repository.findLatestByName("eventName")).isEqualTo(event5);
        Assertions.assertThat(repository.findByNameDesc("eventName", 10)).hasSize(5);
        Assertions.assertThat(repository.findByNameDesc("eventName", 10).get(0)).isEqualTo(event5);

        repository.clearByTtl();

        Assertions.assertThat(repository.findLatestByName("eventName")).isEqualTo(event5);
        Assertions.assertThat(repository.findByNameDesc("eventName", 10)).hasSize(3);
        Assertions.assertThat(repository.findByNameDesc("eventName", 10).get(0)).isEqualTo(event5);
        Assertions.assertThat(repository.findByNameDesc("eventName", 10).get(1)).isEqualTo(event4);
        Assertions.assertThat(repository.findByNameDesc("eventName", 10).get(2)).isEqualTo(event3);

        repository.clearByTtl();

        Assertions.assertThat(repository.findByNameDesc("eventName", 10)).hasSize(3);
        Assertions.assertThat(repository.findByNameDesc("eventName", 10).get(0)).isEqualTo(event5);
    }
}
