package cl.orioneta.notifications.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record NotificationResponseDTO(
        UUID id,
        UUID userId,
        String type,
        String title,
        String body,
        boolean read,
        LocalDateTime createdAt,
        LocalDateTime readAt,
        UUID senderId,
        String senderName,
        String senderAvatar,
        UUID conversationId
) {
}
