package cl.orioneta.notifications.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cl.orioneta.notifications.application.dto.NotificationRequestDTO;
import cl.orioneta.notifications.domain.exception.NotificationNotFoundException;
import cl.orioneta.notifications.domain.model.Notification;
import cl.orioneta.notifications.domain.repository.NotificationRepositoryPort;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Pruebas de los casos de uso de notificaciones.
 */
@ExtendWith(MockitoExtension.class)
class NotificationUseCaseTest {

    private final Faker faker = new Faker();

    @Mock
    private NotificationRepositoryPort notificationRepositoryPort;

    private CreateNotificationUseCase createUseCase;
    private MarkNotificationAsReadUseCase markAsReadUseCase;
    private FindNotificationsUseCase findUseCase;

    @BeforeEach
    void setUp() {
        createUseCase = new CreateNotificationUseCase(notificationRepositoryPort);
        markAsReadUseCase = new MarkNotificationAsReadUseCase(notificationRepositoryPort);
        findUseCase = new FindNotificationsUseCase(notificationRepositoryPort);
    }

    @Test
    void createPersistsNewNotification() {
        NotificationRequestDTO request = new NotificationRequestDTO(
                UUID.randomUUID(),
                "FRIEND_REQUEST",
                faker.lorem().sentence(),
                faker.lorem().sentence(),
                null,
                null,
                null,
                null
        );
        when(notificationRepositoryPort.save(any(Notification.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Notification notification = createUseCase.execute(request);

        verify(notificationRepositoryPort).save(any(Notification.class));
        assertThat(notification.getUserId()).isEqualTo(request.userId());
        assertThat(notification.getType()).isEqualTo(request.type());
        assertThat(notification.isRead()).isFalse();
    }

    @Test
    void markAsReadUpdatesExistingNotification() {
        Notification notification = Notification.create(
                UUID.randomUUID(),
                "MESSAGE",
                faker.lorem().sentence(),
                faker.lorem().sentence()
        );
        when(notificationRepositoryPort.findById(notification.getId())).thenReturn(Optional.of(notification));
        when(notificationRepositoryPort.save(any(Notification.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Notification updated = markAsReadUseCase.execute(notification.getId());

        assertThat(updated.isRead()).isTrue();
        assertThat(updated.getReadAt()).isNotNull();
    }

    @Test
    void markAsReadFailsWhenNotificationDoesNotExist() {
        UUID notificationId = UUID.randomUUID();
        when(notificationRepositoryPort.findById(notificationId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> markAsReadUseCase.execute(notificationId))
                .isInstanceOf(NotificationNotFoundException.class)
                .hasMessage("Notificacion no encontrada");
    }

    @Test
    void findReturnsUserNotifications() {
        UUID userId = UUID.randomUUID();
        List<Notification> notifications = List.of(
                Notification.create(userId, "MESSAGE", "Uno", "Body"),
                Notification.create(userId, "MESSAGE", "Dos", "Body")
        );
        when(notificationRepositoryPort.findByUserId(userId)).thenReturn(notifications);

        assertThat(findUseCase.execute(userId)).containsExactlyElementsOf(notifications);
    }
}
