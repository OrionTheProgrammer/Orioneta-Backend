package cl.orioneta.customization.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserCustomizationDTO(
        UUID id,
        UUID userId,
        String activeGlobalThemeId,
        String activeFontId,
        Integer animationLevel,
        Boolean compactMode,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
