package cl.orioneta.notifications.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.UUID;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;

/**
 * Pruebas de reglas puras de notificaciones.
 */
class NotificationTest {

    private final Faker faker = new Faker();

    @Test
    void createBuildsUnreadNotificationWithTrimmedValues() {
        String title = "  " + faker.lorem().sentence() + "  ";

        Notification notification = Notification.create(
                UUID.randomUUID(),
                " MESSAGE ",
                title,
                "  cuerpo  "
        );

        assertThat(notification.getType()).isEqualTo("MESSAGE");
        assertThat(notification.getTitle()).isEqualTo(title.trim());
        assertThat(notification.getBody()).isEqualTo("cuerpo");
        assertThat(notification.isRead()).isFalse();
        assertThat(notification.getReadAt()).isNull();
    }

    @Test
    void markAsReadStoresReadDateOnlyOnce() {
        Notification notification = Notification.create(
                UUID.randomUUID(),
                "MESSAGE",
                faker.lorem().sentence(),
                faker.lorem().sentence()
        );

        notification.markAsRead();

        assertThat(notification.isRead()).isTrue();
        assertThat(notification.getReadAt()).isNotNull();
    }

    @Test
    void createRejectsBlankTitle() {
        assertThatThrownBy(() -> Notification.create(UUID.randomUUID(), "MESSAGE", " ", "body"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("El titulo es obligatorio");
    }
}
