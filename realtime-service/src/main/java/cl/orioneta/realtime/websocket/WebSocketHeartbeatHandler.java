package cl.orioneta.realtime.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PingMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketHeartbeatHandler {

    private static final Logger log = LoggerFactory.getLogger(WebSocketHeartbeatHandler.class);
    private static final Duration SESSION_TIMEOUT = Duration.ofSeconds(30);

    private final Map<String, Instant> lastPongBySession = new ConcurrentHashMap<>();
    private final WebSocketSessionRegistry sessionRegistry;

    public WebSocketHeartbeatHandler(WebSocketSessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    public void onPong(WebSocketSession session) {
        lastPongBySession.put(session.getId(), Instant.now());
    }

    public void registerSession(WebSocketSession session) {
        lastPongBySession.put(session.getId(), Instant.now());
    }

    public void unregisterSession(String sessionId) {
        lastPongBySession.remove(sessionId);
    }

    @Scheduled(fixedRate = 10000)
    public void heartbeatTick() {
        Instant now = Instant.now();

        for (WebSocketSession session : sessionRegistry.getAllSessions()) {
            if (!session.isOpen()) {
                continue;
            }

            Instant lastPong = lastPongBySession.get(session.getId());
            if (lastPong != null && Duration.between(lastPong, now).compareTo(SESSION_TIMEOUT) > 0) {
                log.warn("[Heartbeat] Sesión {} sin Pong por {}s (proxy gateway no reenvía Pongs). Enviando Ping de todos modos.",
                        session.getId(), Duration.between(lastPong, now).toSeconds());
            }

            sendPing(session);
        }
    }

    private void sendPing(WebSocketSession session) {
        try {
            synchronized (session) {
                session.sendMessage(new PingMessage(ByteBuffer.wrap(new byte[0])));
            }
        } catch (IOException ignored) {
        }
    }

    private void closeQuietly(WebSocketSession session, CloseStatus status) {
        try {
            session.close(status);
        } catch (IOException ignored) {
        }
    }
}
