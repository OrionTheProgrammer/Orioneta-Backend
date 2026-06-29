package cl.orioneta.realtime.service;

import cl.orioneta.realtime.client.ConversationParticipantClient;
import cl.orioneta.realtime.dto.PresenceEventDTO;
import cl.orioneta.realtime.dto.RealtimeMessageDTO;
import cl.orioneta.realtime.websocket.WebSocketSessionRegistry;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Decide hacia que usuarios se envia cada evento de realtime.
 *
 * <p>El handler WebSocket solo recibe texto. Esta clase parsea el JSON,
 * valida el destino y evita broadcasts innecesarios para que un mensaje de un
 * chat no refresque pantallas de usuarios que no participan en esa conversacion.</p>
 */
@Service
public class RealtimeEventDispatcher {

    private final ConversationParticipantClient conversationParticipantClient;
    private final WebSocketSessionRegistry sessionRegistry;
    private final ObjectMapper objectMapper;

    public RealtimeEventDispatcher(
            ConversationParticipantClient conversationParticipantClient,
            WebSocketSessionRegistry sessionRegistry,
            ObjectMapper objectMapper
    ) {
        this.conversationParticipantClient = conversationParticipantClient;
        this.sessionRegistry = sessionRegistry;
        this.objectMapper = objectMapper;
    }

    /**
     * Procesa un payload enviado por un cliente WebSocket.
     *
     * @param session sesion que envio el evento.
     * @param connectedUserId usuario asociado a la sesion.
     * @param payload JSON recibido desde el frontend.
     */
    public void dispatchClientPayload(WebSocketSession session, UUID connectedUserId, String payload) {
        try {
            RealtimeMessageDTO event = objectMapper.readValue(payload, RealtimeMessageDTO.class)
                    .withDefaults(connectedUserId);

            dispatchEvent(event, true);
        } catch (JsonProcessingException exception) {
            sendError(session, "El evento de realtime no tiene un formato JSON valido");
        } catch (IllegalArgumentException | IllegalStateException exception) {
            sendError(session, exception.getMessage());
        }
    }

    /**
     * Procesa eventos generados por otros componentes del backend.
     *
     * @param payload JSON del evento.
     */
    public void dispatchSystemPayload(String payload) {
        try {
            RealtimeMessageDTO event = objectMapper.readValue(payload, RealtimeMessageDTO.class)
                    .withDefaults(null);

            dispatchSystemEvent(event);
        } catch (JsonProcessingException exception) {
            throw new IllegalArgumentException("El evento de realtime no tiene un formato JSON valido", exception);
        }
    }

    /**
     * Procesa un evento ya tipado generado por otro servicio del backend.
     *
     * @param event evento listo para enviar.
     */
    public void dispatchSystemEvent(RealtimeMessageDTO event) {
        dispatchEvent(event.withDefaults(null), false);
    }

    /**
     * Publica un cambio de presencia para las sesiones conectadas.
     *
     * @param event evento de presencia.
     */
    public void publishPresence(PresenceEventDTO event) {
        sessionRegistry.broadcast(toJson(event));
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
        List<UUID> participantIds = conversationParticipantClient.findParticipantIds(event.conversationId());

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
