package cl.orioneta.friendships.infrastructure.messaging;

import cl.orioneta.friendships.app.event.FriendshipEventPublisher;
import cl.orioneta.friendships.domain.event.FriendRequestAcceptedEvent;
import cl.orioneta.friendships.domain.event.FriendRequestSentEvent;
import cl.orioneta.friendships.infrastructure.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

/**
 * Publicador RabbitMQ de eventos de amistad.
 */
@Component
public class RabbitFriendshipEventPublisher implements FriendshipEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public RabbitFriendshipEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publishFriendRequestSent(FriendRequestSentEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.FRIENDSHIP_EXCHANGE,
                RabbitMQConfig.FRIEND_REQUEST_SENT_ROUTING_KEY,
                new cl.orioneta.shared.events.FriendRequestSentEvent(
                        event.requestId(),
                        event.senderUserId(),
                        event.receiverUserId(),
                        event.createdAt().atZone(ZoneId.systemDefault()).toInstant()
                )
        );
    }

    @Override
    public void publishFriendRequestAccepted(FriendRequestAcceptedEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.FRIENDSHIP_EXCHANGE,
                RabbitMQConfig.FRIEND_REQUEST_ACCEPTED_ROUTING_KEY,
                new cl.orioneta.shared.events.FriendRequestAcceptedEvent(
                        event.requestId(),
                        event.senderUserId(),
                        event.receiverUserId(),
                        event.acceptedAt().atZone(ZoneId.systemDefault()).toInstant()
                )
        );
    }
}
