package cl.orioneta.messages.application.dto;

import cl.orioneta.messages.domain.model.MessageStatus;
import cl.orioneta.messages.domain.model.MessageType;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Respuesta publica de un mensaje.
 */
public record MessageResponseDTO(
        UUID id,
        UUID conversationId,
        UUID senderId,
        String content,
        MessageType type,
        MessageStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt
) {
}
