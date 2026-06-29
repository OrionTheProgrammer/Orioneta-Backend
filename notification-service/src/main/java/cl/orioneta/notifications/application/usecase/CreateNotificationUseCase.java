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
        if (request.senderId() != null || request.senderName() != null || request.conversationId() != null) {
            return notificationRepositoryPort.save(Notification.createWithSender(
                    request.userId(),
                    request.type(),
                    request.title(),
                    request.body(),
                    request.senderId(),
                    request.senderName(),
                    request.senderAvatar(),
                    request.conversationId()
            ));
        }
        return notificationRepositoryPort.save(Notification.create(
                request.userId(),
                request.type(),
                request.title(),
                request.body()
        ));
    }
}
