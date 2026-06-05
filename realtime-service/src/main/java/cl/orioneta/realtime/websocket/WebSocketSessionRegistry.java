package cl.orioneta.realtime.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class WebSocketSessionRegistry {

    private final ConcurrentMap<UUID, Set<WebSocketSession>> sessionsByUser = new ConcurrentHashMap<>();

    public void register(UUID userId, WebSocketSession session) {
        sessionsByUser.computeIfAbsent(userId, ignored -> ConcurrentHashMap.newKeySet()).add(session);
    }

    public void unregister(UUID userId, WebSocketSession session) {
        Set<WebSocketSession> sessions = sessionsByUser.get(userId);
        if (sessions != null) {
            sessions.remove(session);
            if (sessions.isEmpty()) {
                sessionsByUser.remove(userId);
            }
        }
    }

    public void sendToUser(UUID userId, String payload) {
        Set<WebSocketSession> sessions = sessionsByUser.getOrDefault(userId, Set.of());
        for (WebSocketSession session : sessions) {
            send(session, payload);
        }
    }

    public void broadcast(String payload) {
        sessionsByUser.values().forEach(sessions -> sessions.forEach(session -> send(session, payload)));
    }

    private void send(WebSocketSession session, String payload) {
        if (!session.isOpen()) {
            return;
        }

        try {
            session.sendMessage(new TextMessage(payload));
        } catch (IOException ignored) {
            // La sesion se limpiara cuando Spring dispare afterConnectionClosed.
        }
    }
}
