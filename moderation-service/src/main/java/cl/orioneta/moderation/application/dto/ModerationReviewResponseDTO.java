package cl.orioneta.moderation.application.dto;

import cl.orioneta.moderation.domain.model.ModerationStatus;
import cl.orioneta.moderation.domain.model.ModerationTargetType;

import java.time.LocalDateTime;
import java.util.UUID;

public record ModerationReviewResponseDTO(
        UUID id,
        UUID targetId,
        ModerationTargetType targetType,
        UUID reviewerId,
        ModerationStatus status,
        String reason,
        LocalDateTime createdAt,
        LocalDateTime resolvedAt
) {
}
