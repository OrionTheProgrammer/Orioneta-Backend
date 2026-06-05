package cl.orioneta.realtime.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final WebSocketSessionRegistry sessionRegistry;

    public ChatWebSocketHandler(WebSocketSessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessionRegistry.register(resolveUserId(session), session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        sessionRegistry.broadcast(message.getPayload());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessionRegistry.unregister(resolveUserId(session), session);
    }

    private UUID resolveUserId(WebSocketSession session) {
        URI uri = session.getUri();
        String rawUserId = uri == null ? null : UriComponentsBuilder.fromUri(uri).build().getQueryParams().getFirst("userId");

        if (rawUserId == null || rawUserId.isBlank()) {
            return UUID.nameUUIDFromBytes(session.getId().getBytes());
        }

        return UUID.fromString(rawUserId);
    }
}
