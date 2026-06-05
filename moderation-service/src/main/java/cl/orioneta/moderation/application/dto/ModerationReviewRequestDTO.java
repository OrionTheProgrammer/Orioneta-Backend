package cl.orioneta.moderation.application.dto;

import cl.orioneta.moderation.domain.model.ModerationStatus;
import cl.orioneta.moderation.domain.model.ModerationTargetType;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ModerationReviewRequestDTO(
        @NotNull(message = "El objetivo es obligatorio")
        UUID targetId,
        @NotNull(message = "El tipo de objetivo es obligatorio")
        ModerationTargetType targetType,
        UUID reviewerId,
        ModerationStatus status,
        String reason
) {
}
