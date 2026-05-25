package cl.orioneta.shared.events;

import java.time.Instant;
import java.util.UUID;

public record MessageSentEvent(
        UUID messageId,
        UUID conversationId,
        UUID senderId,
        Instant occurredAt
) {
}
