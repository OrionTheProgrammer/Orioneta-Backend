package cl.orioneta.notifications.application.usecase;

import cl.orioneta.notifications.domain.model.Notification;
import cl.orioneta.notifications.domain.repository.NotificationRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FindNotificationsUseCase {

    private final NotificationRepositoryPort notificationRepositoryPort;

    public FindNotificationsUseCase(NotificationRepositoryPort notificationRepositoryPort) {
        this.notificationRepositoryPort = notificationRepositoryPort;
    }

    public List<Notification> execute(UUID userId) {
        return notificationRepositoryPort.findByUserId(userId);
    }
}
