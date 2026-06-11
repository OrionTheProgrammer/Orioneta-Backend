package cl.orioneta.shared.events;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageSentEvent(
        UUID messageId,
        UUID conversationId,
        UUID senderId,
        String content,
        String messageType,
        List<UUID> participantIds,
        Instant occurredAt
) {
}
