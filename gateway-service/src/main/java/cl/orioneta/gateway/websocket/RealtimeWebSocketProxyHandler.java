package cl.orioneta.gateway.websocket;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.net.URI;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Proxy WebSocket entre el frontend y realtime-service.
 *
 * <p>Spring Cloud Gateway MVC enruta bien HTTP, pero no hace upgrade WebSocket
 * con rutas declarativas. Este handler mantiene el gateway como punto unico de
 * entrada y delega la sesion real a realtime-service sin agregar logica de chat.</p>
 */
@Component
public class RealtimeWebSocketProxyHandler extends TextWebSocketHandler {

    private static final String UPSTREAM_SESSION_ATTRIBUTE = "realtimeUpstreamSession";
    private static final String PENDING_MESSAGES_ATTRIBUTE = "realtimePendingMessages";

    private final StandardWebSocketClient webSocketClient;
    private final String realtimeServiceUrl;

    public RealtimeWebSocketProxyHandler(
            @Value("${orioneta.routes.realtime:${ORIONETA_REALTIME_URL:http://localhost:8091}}") String realtimeServiceUrl
    ) {
        this.webSocketClient = new StandardWebSocketClient();
        this.realtimeServiceUrl = realtimeServiceUrl;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession clientSession) {
        Queue<String> pendingMessages = new ConcurrentLinkedQueue<>();
        clientSession.getAttributes().put(PENDING_MESSAGES_ATTRIBUTE, pendingMessages);

        webSocketClient.execute(
                new UpstreamRealtimeHandler(clientSession),
                new WebSocketHttpHeaders(),
                buildUpstreamUri(clientSession)
        ).whenComplete((upstreamSession, error) -> {
            if (error != null) {
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

        if (upstreamSession == null || !upstreamSession.isOpen()) {
            findPendingMessages(clientSession).add(message.getPayload());
            return;
        }

        send(upstreamSession, message.getPayload());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession clientSession, CloseStatus status) {
        closeQuietly(findUpstreamSession(clientSession), status);
    }

    @Override
    public void handleTransportError(WebSocketSession clientSession, Throwable exception) {
        closeQuietly(findUpstreamSession(clientSession), CloseStatus.SERVER_ERROR);
    }

    private URI buildUpstreamUri(WebSocketSession clientSession) {
        URI clientUri = clientSession.getUri();
        String query = clientUri == null ? null : clientUri.getRawQuery();
        String normalizedBaseUrl = realtimeServiceUrl
                .replaceFirst("^http://", "ws://")
                .replaceFirst("^https://", "wss://")
                .replaceAll("/+$", "");

        String uri = normalizedBaseUrl + "/ws/chat";
        if (query != null && !query.isBlank()) {
            uri = uri + "?" + query;
        }

        return URI.create(uri);
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
            // La sesion ya esta en cierre o fue cerrada por el otro extremo.
        }
    }

    private final class UpstreamRealtimeHandler extends TextWebSocketHandler {

        private final WebSocketSession clientSession;

        private UpstreamRealtimeHandler(WebSocketSession clientSession) {
            this.clientSession = clientSession;
        }

        @Override
        protected void handleTextMessage(WebSocketSession upstreamSession, TextMessage message) {
            send(clientSession, message.getPayload());
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
