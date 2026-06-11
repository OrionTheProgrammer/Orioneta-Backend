package cl.orioneta.realtime.websocket;

import cl.orioneta.realtime.dto.PresenceEventDTO;
import cl.orioneta.realtime.service.RealtimeEventDispatcher;
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

    private static final String USER_ID_ATTRIBUTE = "userId";

    private final WebSocketSessionRegistry sessionRegistry;
    private final RealtimeEventDispatcher realtimeEventDispatcher;

    public ChatWebSocketHandler(
            WebSocketSessionRegistry sessionRegistry,
            RealtimeEventDispatcher realtimeEventDispatcher
    ) {
        this.sessionRegistry = sessionRegistry;
        this.realtimeEventDispatcher = realtimeEventDispatcher;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        UUID userId = resolveUserId(session);
        session.getAttributes().put(USER_ID_ATTRIBUTE, userId);

        sessionRegistry.register(userId, session);
        realtimeEventDispatcher.publishPresence(PresenceEventDTO.connected(userId));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        realtimeEventDispatcher.dispatchClientPayload(session, resolveStoredUserId(session), message.getPayload());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        UUID userId = resolveStoredUserId(session);

        sessionRegistry.unregister(userId, session);
        if (!sessionRegistry.isUserOnline(userId)) {
            realtimeEventDispatcher.publishPresence(PresenceEventDTO.disconnected(userId));
        }
    }

    private UUID resolveUserId(WebSocketSession session) {
        URI uri = session.getUri();
        String rawUserId = uri == null ? null : UriComponentsBuilder.fromUri(uri).build().getQueryParams().getFirst("userId");

        if (rawUserId == null || rawUserId.isBlank()) {
            return UUID.nameUUIDFromBytes(session.getId().getBytes());
        }

        return UUID.fromString(rawUserId);
    }

    private UUID resolveStoredUserId(WebSocketSession session) {
        Object storedUserId = session.getAttributes().get(USER_ID_ATTRIBUTE);

        if (storedUserId instanceof UUID userId) {
            return userId;
        }

        return resolveUserId(session);
    }
}
