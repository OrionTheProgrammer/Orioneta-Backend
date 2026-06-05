package cl.orioneta.notifications.application.usecase;

import cl.orioneta.notifications.application.dto.NotificationRequestDTO;
import cl.orioneta.notifications.domain.model.Notification;
import cl.orioneta.notifications.domain.repository.NotificationRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class CreateNotificationUseCase {

    private final NotificationRepositoryPort notificationRepositoryPort;

    public CreateNotificationUseCase(NotificationRepositoryPort notificationRepositoryPort) {
        this.notificationRepositoryPort = notificationRepositoryPort;
    }

    public Notification execute(NotificationRequestDTO request) {
        return notificationRepositoryPort.save(Notification.create(
                request.userId(),
                request.type(),
                request.title(),
                request.body()
        ));
    }
}
