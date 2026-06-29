package cl.orioneta.notifications.infrastructure.messaging;

import cl.orioneta.notifications.application.dto.NotificationRequestDTO;
import cl.orioneta.notifications.application.usecase.CreateNotificationUseCase;
import cl.orioneta.notifications.infrastructure.client.UserServiceClient;
import cl.orioneta.shared.events.FriendRequestAcceptedEvent;
import cl.orioneta.shared.events.FriendRequestSentEvent;
import cl.orioneta.shared.events.MessageSentEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(NotificationEventConsumer.class);

    private final CreateNotificationUseCase createNotificationUseCase;
    private final UserServiceClient userServiceClient;

    public NotificationEventConsumer(
            CreateNotificationUseCase createNotificationUseCase,
            UserServiceClient userServiceClient
    ) {
        this.createNotificationUseCase = createNotificationUseCase;
        this.userServiceClient = userServiceClient;
    }

    @RabbitListener(queues = "${app.rabbitmq.queue.notifications.messages}")
    public void consumeMessageSent(MessageSentEvent event) {
        UserServiceClient.UserProfile senderProfile = userServiceClient.getUserProfile(event.senderId());

        event.participantIds().stream()
                .filter(participantId -> !participantId.equals(event.senderId()))
                .forEach(participantId -> {
                    createNotificationUseCase.execute(new NotificationRequestDTO(
                            participantId,
                            "MESSAGE_SENT",
                            senderProfile != null ? senderProfile.displayName() : "Nuevo mensaje",
                            "Tienes un mensaje nuevo en una conversacion.",
                            event.senderId(),
                            senderProfile != null ? senderProfile.displayName() : null,
                            senderProfile != null ? senderProfile.profilePhoto() : null,
                            event.conversationId()
                    ));
                });
    }

    @RabbitListener(queues = "${app.rabbitmq.queue.notifications.friend-requests.sent}")
    public void consumeFriendRequestSent(FriendRequestSentEvent event) {
        UserServiceClient.UserProfile senderProfile = userServiceClient.getUserProfile(event.senderUserId());

        createNotificationUseCase.execute(new NotificationRequestDTO(
                event.receiverUserId(),
                "FRIEND_REQUEST_SENT",
                senderProfile != null ? "Solicitud de amistad" : "Solicitud de amistad",
                senderProfile != null
                        ? senderProfile.displayName() + " quiere ser tu amigo"
                        : "Alguien quiere ser tu amigo",
                event.senderUserId(),
                senderProfile != null ? senderProfile.displayName() : null,
                senderProfile != null ? senderProfile.profilePhoto() : null,
                null
        ));
    }

    @RabbitListener(queues = "${app.rabbitmq.queue.notifications.friend-requests.accepted}")
    public void consumeFriendRequestAccepted(FriendRequestAcceptedEvent event) {
        UserServiceClient.UserProfile acceptorProfile = userServiceClient.getUserProfile(event.receiverUserId());

        createNotificationUseCase.execute(new NotificationRequestDTO(
                event.senderUserId(),
                "FRIEND_REQUEST_ACCEPTED",
                acceptorProfile != null ? "Solicitud aceptada" : "Solicitud aceptada",
                acceptorProfile != null
                        ? acceptorProfile.displayName() + " acepto tu solicitud"
                        : "Tu solicitud de amistad fue aceptada",
                event.receiverUserId(),
                acceptorProfile != null ? acceptorProfile.displayName() : null,
                acceptorProfile != null ? acceptorProfile.profilePhoto() : null,
                null
        ));
    }
}
