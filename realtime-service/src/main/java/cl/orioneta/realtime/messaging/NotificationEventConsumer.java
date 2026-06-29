package cl.orioneta.realtime.messaging;

import cl.orioneta.realtime.websocket.WebSocketSessionRegistry;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class NotificationEventConsumer {

    private final WebSocketSessionRegistry sessionRegistry;

    public NotificationEventConsumer(WebSocketSessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    public void sendNotificationToUser(UUID userId, String payload) {
        sessionRegistry.sendToUser(userId, payload);
    }
}
