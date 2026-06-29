package cl.orioneta.realtime.websocket;

import cl.orioneta.realtime.dto.UserConnectionDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class WebSocketSessionRegistry {

    private final ConcurrentMap<UUID, Set<WebSocketSession>> sessionsByUser = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, UserConnectionDTO> connectionsBySession = new ConcurrentHashMap<>();

    public void register(UUID userId, WebSocketSession session) {
        sessionsByUser.computeIfAbsent(userId, ignored -> ConcurrentHashMap.newKeySet()).add(session);
        connectionsBySession.put(session.getId(), new UserConnectionDTO(userId, session.getId(), Instant.now()));
    }

    public void unregister(UUID userId, WebSocketSession session) {
        Set<WebSocketSession> sessions = sessionsByUser.get(userId);
        if (sessions != null) {
            sessions.remove(session);
            if (sessions.isEmpty()) {
                sessionsByUser.remove(userId);
            }
        }

        connectionsBySession.remove(session.getId());
    }

    public void sendToUser(UUID userId, String payload) {
        Set<WebSocketSession> sessions = sessionsByUser.getOrDefault(userId, Set.of());
        for (WebSocketSession session : sessions) {
            send(session, payload);
        }
    }

    public void sendToUsers(Collection<UUID> userIds, String payload) {
        userIds.forEach(userId -> sendToUser(userId, payload));
    }

    public void sendToSession(WebSocketSession session, String payload) {
        send(session, payload);
    }

    public void broadcast(String payload) {
        sessionsByUser.values().forEach(sessions -> sessions.forEach(session -> send(session, payload)));
    }

    public boolean isUserOnline(UUID userId) {
        return sessionsByUser.containsKey(userId);
    }

    public List<UserConnectionDTO> findAllConnections() {
        return List.copyOf(connectionsBySession.values());
    }

    public List<UserConnectionDTO> findConnectionsByUser(UUID userId) {
        return connectionsBySession.values()
                .stream()
                .filter(connection -> connection.userId().equals(userId))
                .toList();
    }

    private void send(WebSocketSession session, String payload) {
        if (!session.isOpen()) {
            return;
        }

        try {
            synchronized (session) {
                session.sendMessage(new TextMessage(payload));
            }
        } catch (IOException ignored) {
            // La sesion se limpiara cuando Spring dispare afterConnectionClosed.
        }
    }
}
