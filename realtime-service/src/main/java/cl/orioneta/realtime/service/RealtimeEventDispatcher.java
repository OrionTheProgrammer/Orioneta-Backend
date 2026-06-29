package cl.orioneta.realtime.service;

import cl.orioneta.realtime.dto.PresenceEventDTO;
import cl.orioneta.realtime.dto.RealtimeMessageDTO;
import cl.orioneta.realtime.websocket.WebSocketSessionRegistry;
import cl.orioneta.shared.events.UserStatusChangedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class RealtimeEventDispatcher {

    private static final Logger log = LoggerFactory.getLogger(RealtimeEventDispatcher.class);

    private static final List<String> TYPING_EVENT_TYPES = List.of("TYPING_START", "TYPING_STOP");
    private static final List<String> CALL_SIGNAL_TYPES = List.of("CALL_OFFER", "CALL_ANSWER", "CALL_ICE_CANDIDATE", "CALL_ENDED", "CALL_DECLINED");

    private final ParticipantCacheService participantCache;
    private final WebSocketSessionRegistry sessionRegistry;
    private final ObjectMapper objectMapper;

    public RealtimeEventDispatcher(
            ParticipantCacheService participantCache,
            WebSocketSessionRegistry sessionRegistry,
            ObjectMapper objectMapper
    ) {
        this.participantCache = participantCache;
        this.sessionRegistry = sessionRegistry;
        this.objectMapper = objectMapper;
    }

    public void dispatchClientPayload(WebSocketSession session, UUID connectedUserId, String payload) {
        try {
            RealtimeMessageDTO event = objectMapper.readValue(payload, RealtimeMessageDTO.class)
                    .withDefaults(connectedUserId);

            log.info("[Signaling] Evento recibido: type={}, conversationId={}, senderId={}, messageType={}, content={}",
                    event.type(), event.conversationId(), event.senderId(), event.messageType(),
                    event.content() != null ? event.content().substring(0, Math.min(event.content().length(), 120)) : null);

            if ("KEEPALIVE".equals(event.type())) {
                return;
            }

            if (TYPING_EVENT_TYPES.contains(event.type())) {
                handleTypingEvent(event);
                return;
            }

            if (CALL_SIGNAL_TYPES.contains(event.type())) {
                handleCallSignalEvent(event);
                return;
            }

            dispatchEvent(event, true);
        } catch (JsonProcessingException exception) {
            sendError(session, "El evento de realtime no tiene un formato JSON valido");
        } catch (IllegalArgumentException | IllegalStateException exception) {
            log.error("[Signaling] Error enrutando evento: {}", exception.getMessage());
            sendError(session, exception.getMessage());
        }
    }

    public void dispatchSystemPayload(String payload) {
        try {
            RealtimeMessageDTO event = objectMapper.readValue(payload, RealtimeMessageDTO.class)
                    .withDefaults(null);

            dispatchSystemEvent(event);
        } catch (JsonProcessingException exception) {
            throw new IllegalArgumentException("El evento de realtime no tiene un formato JSON valido", exception);
        }
    }

    public void dispatchSystemEvent(RealtimeMessageDTO event) {
        dispatchEvent(event.withDefaults(null), false);
    }

    public void publishPresence(PresenceEventDTO event) {
        sessionRegistry.broadcast(toJson(event));
    }

    public void broadcastProfileChanged(UserStatusChangedEvent event) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("type", "USER_STATUS_CHANGED");
        payload.put("userId", event.userId());
        payload.put("changeType", event.type());
        payload.put("newValue", event.newValue());
        payload.put("occurredAt", event.occurredAt());
        sessionRegistry.broadcast(toJson(payload));
    }

    private void handleCallSignalEvent(RealtimeMessageDTO event) {
        if (event.conversationId() == null || event.senderId() == null) {
            log.warn("[Signaling] CALL_SIGNAL ignorado: conversationId o senderId nulo");
            return;
        }
        List<UUID> participantIds = participantCache.getParticipantIds(event.conversationId());
        List<UUID> targets = participantIds.stream()
                .filter(pid -> !pid.equals(event.senderId()))
                .toList();
        log.info("[Signaling] Enviando {} a {} receptores (excluyendo remitente {}). Participantes totales: {}",
                event.type(), targets.size(), event.senderId(), participantIds.size());
        if (!targets.isEmpty()) {
            String json = toJson(event);
            targets.forEach(uid -> {
                log.info("[Signaling] Enviando {} a userId={}", event.type(), uid);
                sessionRegistry.sendToUser(uid, json);
            });
        } else {
            log.warn("[Signaling] No hay receptores para {} (conversationId={}, senderId={})",
                    event.type(), event.conversationId(), event.senderId());
        }
    }

    private void handleTypingEvent(RealtimeMessageDTO event) {
        if (event.conversationId() == null || event.senderId() == null) return;
        List<UUID> participantIds = participantCache.getParticipantIds(event.conversationId());
        List<UUID> targets = participantIds.stream()
                .filter(pid -> !pid.equals(event.senderId()))
                .toList();
        if (!targets.isEmpty()) {
            sessionRegistry.sendToUsers(targets, toJson(event));
        }
    }

    private void dispatchEvent(RealtimeMessageDTO event, boolean validateSenderMembership) {
        if (event.conversationId() != null) {
            sendToConversation(event, validateSenderMembership);
            return;
        }

        if (event.targetUserId() != null) {
            sendToDirectTarget(event);
            return;
        }

        throw new IllegalArgumentException("El evento debe indicar conversationId o targetUserId");
    }

    private void sendToConversation(RealtimeMessageDTO event, boolean validateSenderMembership) {
        List<UUID> participantIds = participantCache.getParticipantIds(event.conversationId());

        if (participantIds.isEmpty()) {
            throw new IllegalStateException("No hay participantes disponibles para enrutar el evento");
        }

        if (validateSenderMembership && event.senderId() != null && !participantIds.contains(event.senderId())) {
            throw new IllegalArgumentException("El usuario conectado no pertenece a la conversacion indicada");
        }

        sessionRegistry.sendToUsers(participantIds, toJson(event));
    }

    private void sendToDirectTarget(RealtimeMessageDTO event) {
        String payload = toJson(event);
        sessionRegistry.sendToUser(event.targetUserId(), payload);

        if (event.senderId() != null && !event.senderId().equals(event.targetUserId())) {
            sessionRegistry.sendToUser(event.senderId(), payload);
        }
    }

    private void sendError(WebSocketSession session, String message) {
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("type", "REALTIME_ERROR");
        error.put("message", message);
        error.put("occurredAt", Instant.now());

        sessionRegistry.sendToSession(session, toJson(error));
    }

    private String toJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("No se pudo serializar el evento de realtime", exception);
        }
    }
}
