package cl.orioneta.realtime.messaging;

import cl.orioneta.realtime.websocket.WebSocketSessionRegistry;
import org.springframework.stereotype.Component;

@Component
public class MessageEventConsumer {

    private final WebSocketSessionRegistry sessionRegistry;

    public MessageEventConsumer(WebSocketSessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    public void broadcastMessageEvent(String payload) {
        sessionRegistry.broadcast(payload);
    }
}
