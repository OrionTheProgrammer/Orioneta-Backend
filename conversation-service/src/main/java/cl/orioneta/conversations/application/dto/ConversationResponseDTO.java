package cl.orioneta.conversations.application.dto;

import cl.orioneta.conversations.domain.model.ConversationType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Respuesta publica de una conversacion.
 */
public record ConversationResponseDTO(
        UUID id,
        ConversationType type,
        String name,
        String description,
        UUID ownerId,
        String avatarUrl,
        String backgroundUrl,
        List<ParticipantDTO> participants,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
