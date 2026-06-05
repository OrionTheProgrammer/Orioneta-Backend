package cl.orioneta.realtime.dto;

import java.time.Instant;
import java.util.UUID;

public record RealtimeMessageDTO(
        String type,
        UUID conversationId,
        UUID senderId,
        String content,
        Instant occurredAt
) {
}
