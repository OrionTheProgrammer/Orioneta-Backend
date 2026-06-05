package cl.orioneta.notifications.application.usecase;

import cl.orioneta.notifications.domain.exception.NotificationNotFoundException;
import cl.orioneta.notifications.domain.model.Notification;
import cl.orioneta.notifications.domain.repository.NotificationRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MarkNotificationAsReadUseCase {

    private final NotificationRepositoryPort notificationRepositoryPort;

    public MarkNotificationAsReadUseCase(NotificationRepositoryPort notificationRepositoryPort) {
        this.notificationRepositoryPort = notificationRepositoryPort;
    }

    public Notification execute(UUID id) {
        Notification notification = notificationRepositoryPort.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException("Notificacion no encontrada"));

        notification.markAsRead();

        return notificationRepositoryPort.save(notification);
    }
}
