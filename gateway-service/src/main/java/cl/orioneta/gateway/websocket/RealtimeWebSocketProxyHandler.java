package cl.orioneta.gateway.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class RealtimeWebSocketProxyHandler extends TextWebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(RealtimeWebSocketProxyHandler.class);
    private static final String UPSTREAM_SESSION_ATTRIBUTE = "realtimeUpstreamSession";
    private static final String PENDING_MESSAGES_ATTRIBUTE = "realtimePendingMessages";

    private final StandardWebSocketClient webSocketClient;
    private final String realtimeServiceUrl;
    private final JwtWebSocketInterceptor jwtValidator;

    public RealtimeWebSocketProxyHandler(
            @Value("${orioneta.routes.realtime:http://localhost:8091}") String realtimeServiceUrl,
            JwtWebSocketInterceptor jwtValidator
    ) {
        this.webSocketClient = new StandardWebSocketClient();
        this.realtimeServiceUrl = realtimeServiceUrl;
        this.jwtValidator = jwtValidator;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession clientSession) {
        UUID userId = jwtValidator.extractUserIdFromUri(clientSession.getUri());

        if (userId == null) {
            log.warn("Conexion WebSocket rechazada: token invalido o ausente");
            closeQuietly(clientSession, CloseStatus.POLICY_VIOLATION);
            return;
        }

        Queue<String> pendingMessages = new ConcurrentLinkedQueue<>();
        clientSession.getAttributes().put(PENDING_MESSAGES_ATTRIBUTE, pendingMessages);

        URI upstreamUri = buildUpstreamUri(clientSession, userId);

        webSocketClient.execute(
                new UpstreamRealtimeHandler(clientSession),
                new WebSocketHttpHeaders(),
                upstreamUri
        ).whenComplete((upstreamSession, error) -> {
            if (error != null) {
                log.error("Error conectando con realtime-service", error);
                closeQuietly(clientSession, CloseStatus.SERVER_ERROR);
                return;
            }

            clientSession.getAttributes().put(UPSTREAM_SESSION_ATTRIBUTE, upstreamSession);
            drainPendingMessages(upstreamSession, pendingMessages);
        });
    }

    @Override
    protected void handleTextMessage(WebSocketSession clientSession, TextMessage message) {
        WebSocketSession upstreamSession = findUpstreamSession(clientSession);
        String payload = message.getPayload();
        String type = extractType(payload);

        if (upstreamSession == null || !upstreamSession.isOpen()) {
            log.info("[WS-Proxy] Cliente->Backend ENCOLADO: type={}, sessionId={}", type, clientSession.getId());
            findPendingMessages(clientSession).add(payload);
            return;
        }

        log.info("[WS-Proxy] Cliente->Backend REENVIADO: type={}, sessionId={}", type, clientSession.getId());
        send(upstreamSession, payload);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession clientSession, CloseStatus status) {
        closeQuietly(findUpstreamSession(clientSession), status);
    }

    @Override
    public void handleTransportError(WebSocketSession clientSession, Throwable exception) {
        closeQuietly(findUpstreamSession(clientSession), CloseStatus.SERVER_ERROR);
    }

    private URI buildUpstreamUri(WebSocketSession clientSession, UUID validatedUserId) {
        String normalizedBaseUrl = realtimeServiceUrl
                .replaceFirst("^http://", "ws://")
                .replaceFirst("^https://", "wss://")
                .replaceAll("/+$", "");

        return URI.create(normalizedBaseUrl + "/ws/chat?userId=" + validatedUserId);
    }

    private void drainPendingMessages(WebSocketSession upstreamSession, Queue<String> pendingMessages) {
        String payload;
        while ((payload = pendingMessages.poll()) != null) {
            send(upstreamSession, payload);
        }
    }

    @SuppressWarnings("unchecked")
    private Queue<String> findPendingMessages(WebSocketSession clientSession) {
        Object pendingMessages = clientSession.getAttributes().get(PENDING_MESSAGES_ATTRIBUTE);

        if (pendingMessages instanceof Queue<?> queue) {
            return (Queue<String>) queue;
        }

        Queue<String> queue = new ConcurrentLinkedQueue<>();
        clientSession.getAttributes().put(PENDING_MESSAGES_ATTRIBUTE, queue);

        return queue;
    }

    private WebSocketSession findUpstreamSession(WebSocketSession clientSession) {
        Object upstreamSession = clientSession.getAttributes().get(UPSTREAM_SESSION_ATTRIBUTE);

        if (upstreamSession instanceof WebSocketSession session) {
            return session;
        }

        return null;
    }

    private void send(WebSocketSession session, String payload) {
        if (session == null || !session.isOpen()) {
            return;
        }

        try {
            synchronized (session) {
                session.sendMessage(new TextMessage(payload));
            }
        } catch (IOException exception) {
            closeQuietly(session, CloseStatus.SERVER_ERROR);
        }
    }

    private void closeQuietly(WebSocketSession session, CloseStatus status) {
        if (session == null || !session.isOpen()) {
            return;
        }

        try {
            session.close(status);
        } catch (IOException ignored) {
        }
    }

    private String extractType(String payload) {
        try {
            int typeIdx = payload.indexOf("\"type\"");
            if (typeIdx == -1) return "UNKNOWN";
            int colonIdx = payload.indexOf(':', typeIdx + 6);
            int commaIdx = payload.indexOf(',', colonIdx);
            if (commaIdx == -1) return payload.substring(colonIdx + 1).trim().replaceAll("[\"}]", "");
            return payload.substring(colonIdx + 1, commaIdx).trim().replaceAll("[\"}]", "");
        } catch (Exception e) {
            return "PARSE_ERROR";
        }
    }

    private final class UpstreamRealtimeHandler extends TextWebSocketHandler {

        private final WebSocketSession clientSession;

        private UpstreamRealtimeHandler(WebSocketSession clientSession) {
            this.clientSession = clientSession;
        }

        @Override
        protected void handleTextMessage(WebSocketSession upstreamSession, TextMessage message) {
            String payload = message.getPayload();
            String type = extractType(payload);
            log.info("[WS-Proxy] Backend->Cliente REENVIADO: type={}, sessionId={}", type, clientSession.getId());
            send(clientSession, payload);
        }

        @Override
        public void afterConnectionClosed(WebSocketSession upstreamSession, CloseStatus status) {
            closeQuietly(clientSession, status);
        }

        @Override
        public void handleTransportError(WebSocketSession upstreamSession, Throwable exception) {
            closeQuietly(clientSession, CloseStatus.SERVER_ERROR);
        }
    }
}
