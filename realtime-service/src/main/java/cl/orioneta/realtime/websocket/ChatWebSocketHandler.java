package cl.orioneta.realtime.websocket;

import cl.orioneta.realtime.dto.PresenceEventDTO;
import cl.orioneta.realtime.service.RealtimeEventDispatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private static final String USER_ID_ATTRIBUTE = "userId";
    private static final int MAX_MESSAGES_PER_MINUTE = 60;
    private static final Duration RATE_WINDOW = Duration.ofMinutes(1);

    private final Map<String, UserRateLimit> rateLimits = new ConcurrentHashMap<>();

    private final WebSocketSessionRegistry sessionRegistry;
    private final WebSocketHeartbeatHandler heartbeatHandler;
    private final RealtimeEventDispatcher realtimeEventDispatcher;

    public ChatWebSocketHandler(
            WebSocketSessionRegistry sessionRegistry,
            WebSocketHeartbeatHandler heartbeatHandler,
            RealtimeEventDispatcher realtimeEventDispatcher
    ) {
        this.sessionRegistry = sessionRegistry;
        this.heartbeatHandler = heartbeatHandler;
        this.realtimeEventDispatcher = realtimeEventDispatcher;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        UUID userId = resolveUserId(session);
        session.getAttributes().put(USER_ID_ATTRIBUTE, userId);

        sessionRegistry.register(userId, session);
        heartbeatHandler.registerSession(session);
        realtimeEventDispatcher.publishPresence(PresenceEventDTO.connected(userId));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        String sessionId = session.getId();
        UserRateLimit rateLimit = rateLimits.computeIfAbsent(sessionId, ignored -> new UserRateLimit());

        if (rateLimit.isExceeded()) {
            sendError(session, "Demasiados mensajes. Intenta de nuevo en un momento.");
            return;
        }

        rateLimit.increment();
        realtimeEventDispatcher.dispatchClientPayload(session, resolveStoredUserId(session), message.getPayload());
    }

    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) {
        heartbeatHandler.onPong(session);
        sessionRegistry.refreshPresence(resolveStoredUserId(session), session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        UUID userId = resolveStoredUserId(session);

        sessionRegistry.unregister(userId, session);
        heartbeatHandler.unregisterSession(session.getId());
        rateLimits.remove(session.getId());

        if (!sessionRegistry.isUserOnline(userId)) {
            realtimeEventDispatcher.publishPresence(PresenceEventDTO.disconnected(userId));
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        UUID userId = resolveStoredUserId(session);
        sessionRegistry.unregister(userId, session);
        heartbeatHandler.unregisterSession(session.getId());
        rateLimits.remove(session.getId());
    }

    private void sendError(WebSocketSession session, String message) {
        try {
            synchronized (session) {
                session.sendMessage(new TextMessage("{\"type\":\"ERROR\",\"message\":\"" + message + "\"}"));
            }
        } catch (Exception ignored) {
        }
    }

    private UUID resolveUserId(WebSocketSession session) {
        URI uri = session.getUri();
        if (uri == null) return UUID.nameUUIDFromBytes(session.getId().getBytes());

        var params = UriComponentsBuilder.fromUri(uri).build().getQueryParams();

        // Intenta ?userId= primero
        String rawUserId = params.getFirst("userId");
        if (rawUserId != null && !rawUserId.isBlank()) {
            return UUID.fromString(rawUserId);
        }

        // Si no, decodifica el JWT del ?token=
        String token = params.getFirst("token");
        if (token != null && !token.isBlank()) {
            try {
                String[] parts = token.split("\\.");
                if (parts.length >= 2) {
                    String payload = new String(java.util.Base64.getUrlDecoder().decode(parts[1]));
                    com.fasterxml.jackson.databind.JsonNode json =
                            new com.fasterxml.jackson.databind.ObjectMapper().readTree(payload);
                    String userId = json.has("userId") ? json.get("userId").asText() : null;
                    if (userId != null && !userId.isBlank()) {
                        return UUID.fromString(userId);
                    }
                }
            } catch (Exception e) {
                // fallback
            }
        }

        return UUID.nameUUIDFromBytes(session.getId().getBytes());
    }

    private UUID resolveStoredUserId(WebSocketSession session) {
        Object storedUserId = session.getAttributes().get(USER_ID_ATTRIBUTE);

        if (storedUserId instanceof UUID userId) {
            return userId;
        }

        return resolveUserId(session);
    }

    private static class UserRateLimit {
        private final Instant windowStart = Instant.now();
        private int count;

        boolean isExceeded() {
            if (Duration.between(windowStart, Instant.now()).compareTo(RATE_WINDOW) > 0) {
                return false;
            }
            return count >= MAX_MESSAGES_PER_MINUTE;
        }

        void increment() {
            count++;
        }
    }
}
