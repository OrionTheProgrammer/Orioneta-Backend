package cl.orioneta.realtime.websocket;

import cl.orioneta.realtime.dto.UserConnectionDTO;
import cl.orioneta.realtime.service.RedisPresenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class WebSocketSessionRegistry {

    private static final Logger log = LoggerFactory.getLogger(WebSocketSessionRegistry.class);

    private final ConcurrentMap<UUID, Set<WebSocketSession>> sessionsByUser = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, WebSocketSession> sessionsById = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, UserConnectionDTO> connectionsBySession = new ConcurrentHashMap<>();
    private final RedisPresenceService redisPresenceService;

    public WebSocketSessionRegistry(RedisPresenceService redisPresenceService) {
        this.redisPresenceService = redisPresenceService;
    }

    public String getInstanceId() {
        return redisPresenceService.getInstanceId();
    }

    public void register(UUID userId, WebSocketSession session) {
        sessionsByUser.computeIfAbsent(userId, ignored -> ConcurrentHashMap.newKeySet()).add(session);
        sessionsById.put(session.getId(), session);
        connectionsBySession.put(session.getId(), new UserConnectionDTO(userId, session.getId(), Instant.now()));
        redisPresenceService.markOnline(userId, session.getId());
    }

    public void unregister(UUID userId, WebSocketSession session) {
        Set<WebSocketSession> sessions = sessionsByUser.get(userId);
        if (sessions != null) {
            sessions.remove(session);
            if (sessions.isEmpty()) {
                sessionsByUser.remove(userId);
            }
        }

        sessionsById.remove(session.getId());
        connectionsBySession.remove(session.getId());
        redisPresenceService.markOffline(userId, session.getId());
    }

    public WebSocketSession getSession(String sessionId) {
        return sessionsById.get(sessionId);
    }

    public Collection<WebSocketSession> getAllSessions() {
        return sessionsById.values();
    }

    public void sendToUser(UUID userId, String payload) {
        Set<WebSocketSession> sessions = sessionsByUser.getOrDefault(userId, Set.of());
        if (sessions.isEmpty()) {
            log.warn("[WS-Send] No hay sesiones WebSocket para userId={}. No se puede enviar el mensaje", userId);
        }
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
        return sessionsByUser.containsKey(userId) || redisPresenceService.isOnline(userId);
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

    public void refreshPresence(UUID userId, WebSocketSession session) {
        redisPresenceService.refreshPresence(userId, session.getId());
    }

    private void send(WebSocketSession session, String payload) {
        if (!session.isOpen()) {
            log.warn("[WS-Send] Intento de enviar mensaje a sesión cerrada: sessionId={}", session.getId());
            return;
        }

        try {
            synchronized (session) {
                session.sendMessage(new org.springframework.web.socket.TextMessage(payload));
            }
        } catch (Exception e) {
            log.error("[WS-Send] Error enviando mensaje a sesión {}: {}", session.getId(), e.getMessage());
        }
    }
}
