package cl.orioneta.customization.application.dto;

import cl.orioneta.customization.domain.model.BubbleStyle;

import java.time.LocalDateTime;
import java.util.UUID;

public record ConversationCustomizationDTO(
        UUID id,
        UUID conversationId,
        UUID userId,
        String activeChatThemeId,
        String activeBackgroundId,
        BubbleStyle bubbleStyle,
        Integer fontSize,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
