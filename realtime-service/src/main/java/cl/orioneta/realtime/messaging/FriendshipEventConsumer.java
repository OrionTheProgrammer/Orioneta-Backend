package cl.orioneta.realtime.messaging;

import cl.orioneta.realtime.config.RabbitMQConfig;
import cl.orioneta.realtime.dto.RealtimeMessageDTO;
import cl.orioneta.realtime.websocket.WebSocketSessionRegistry;
import cl.orioneta.shared.events.FriendRequestAcceptedEvent;
import cl.orioneta.shared.events.FriendRequestSentEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class FriendshipEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(FriendshipEventConsumer.class);

    private final WebSocketSessionRegistry sessionRegistry;
    private final ObjectMapper objectMapper;

    public FriendshipEventConsumer(WebSocketSessionRegistry sessionRegistry, ObjectMapper objectMapper) {
        this.sessionRegistry = sessionRegistry;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = RabbitMQConfig.REALTIME_FRIEND_REQUEST_SENT_QUEUE)
    public void consumeFriendRequestSent(FriendRequestSentEvent event) {
        try {
            String payload = objectMapper.writeValueAsString(new RealtimeMessageDTO(
                    "FRIEND_REQUEST_SENT",
                    null,
                    null,
                    event.senderUserId(),
                    event.receiverUserId(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    event.occurredAt()
            ));
            sessionRegistry.sendToUser(event.receiverUserId(), payload);
        } catch (JsonProcessingException e) {
            log.warn("Error serializing friend request sent event", e);
        }
    }

    @RabbitListener(queues = RabbitMQConfig.REALTIME_FRIEND_REQUEST_ACCEPTED_QUEUE)
    public void consumeFriendRequestAccepted(FriendRequestAcceptedEvent event) {
        try {
            String payload = objectMapper.writeValueAsString(new RealtimeMessageDTO(
                    "FRIEND_REQUEST_ACCEPTED",
                    null,
                    null,
                    event.receiverUserId(),
                    event.senderUserId(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    event.occurredAt()
            ));
            sessionRegistry.sendToUser(event.senderUserId(), payload);
        } catch (JsonProcessingException e) {
            log.warn("Error serializing friend request accepted event", e);
        }
    }
}
