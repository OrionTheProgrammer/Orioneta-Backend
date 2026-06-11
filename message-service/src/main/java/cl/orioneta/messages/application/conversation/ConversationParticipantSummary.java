package cl.orioneta.messages.application.conversation;

import java.util.UUID;

/**
 * Participante minimo que message-service necesita conocer para validar
 * permisos antes de guardar mensajes.
 */
public record ConversationParticipantSummary(UUID userId) {
}
