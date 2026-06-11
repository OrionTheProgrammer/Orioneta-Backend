package cl.orioneta.notifications.infrastructure.messaging;

import cl.orioneta.notifications.application.dto.NotificationRequestDTO;
import cl.orioneta.notifications.application.usecase.CreateNotificationUseCase;
import cl.orioneta.notifications.infrastructure.config.RabbitMQConfig;
import cl.orioneta.shared.events.FriendRequestAcceptedEvent;
import cl.orioneta.shared.events.FriendRequestSentEvent;
import cl.orioneta.shared.events.MessageSentEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class NotificationEventConsumer {

    private final CreateNotificationUseCase createNotificationUseCase;

    public NotificationEventConsumer(CreateNotificationUseCase createNotificationUseCase) {
        this.createNotificationUseCase = createNotificationUseCase;
    }

    @RabbitListener(queues = RabbitMQConfig.NOTIFICATION_MESSAGE_QUEUE)
    public void consumeMessageSent(MessageSentEvent event) {
        participantsWithoutSender(event.participantIds(), event.senderId())
                .forEach(userId -> createNotificationUseCase.execute(new NotificationRequestDTO(
                        userId,
                        "MESSAGE_SENT",
                        "Nuevo mensaje",
                        "Tienes un mensaje nuevo en una conversacion."
                )));
    }

    @RabbitListener(queues = RabbitMQConfig.NOTIFICATION_FRIEND_REQUEST_SENT_QUEUE)
    public void consumeFriendRequestSent(FriendRequestSentEvent event) {
        createNotificationUseCase.execute(new NotificationRequestDTO(
                event.receiverUserId(),
                "FRIEND_REQUEST_SENT",
                "Nueva solicitud de amistad",
                "Un usuario quiere agregarte como amigo."
        ));
    }

    @RabbitListener(queues = RabbitMQConfig.NOTIFICATION_FRIEND_REQUEST_ACCEPTED_QUEUE)
    public void consumeFriendRequestAccepted(FriendRequestAcceptedEvent event) {
        createNotificationUseCase.execute(new NotificationRequestDTO(
                event.senderUserId(),
                "FRIEND_REQUEST_ACCEPTED",
                "Solicitud aceptada",
                "Tu solicitud de amistad fue aceptada."
        ));
    }

    private List<UUID> participantsWithoutSender(List<UUID> participantIds, UUID senderId) {
        if (participantIds == null) {
            return List.of();
        }

        return participantIds.stream()
                .filter(userId -> !userId.equals(senderId))
                .toList();
    }
}
