package cl.orioneta.conversations.application.dto;

import cl.orioneta.conversations.domain.model.ParticipantRole;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Participante devuelto por la API.
 */
public record ParticipantDTO(
        UUID id,
        UUID userId,
        ParticipantRole role,
        LocalDateTime joinedAt,
        boolean muted,
        boolean deletedForUser
) {
}
