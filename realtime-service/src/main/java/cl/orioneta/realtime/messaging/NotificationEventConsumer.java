package cl.orioneta.realtime.messaging;

import cl.orioneta.realtime.config.RabbitMQConfig;
import cl.orioneta.realtime.websocket.WebSocketSessionRegistry;
import cl.orioneta.shared.events.NotificationCreatedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class NotificationEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(NotificationEventConsumer.class);

    private final WebSocketSessionRegistry sessionRegistry;
    private final ObjectMapper objectMapper;

    public NotificationEventConsumer(
            WebSocketSessionRegistry sessionRegistry,
            ObjectMapper objectMapper
    ) {
        this.sessionRegistry = sessionRegistry;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = RabbitMQConfig.REALTIME_NOTIFICATION_QUEUE)
    public void consumeNotificationCreated(NotificationCreatedEvent event) {
        try {
            String payload = objectMapper.writeValueAsString(event);
            sessionRegistry.sendToUser(event.userId(), payload);
        } catch (JsonProcessingException e) {
            log.warn("Error serializing notification event", e);
        }
    }

    public void sendNotificationToUser(UUID userId, String payload) {
        sessionRegistry.sendToUser(userId, payload);
    }
}
