package cl.orioneta.shared.events;

import java.time.Instant;
import java.util.UUID;

public record ConversationCreatedEvent(
        UUID conversationId,
        UUID createdBy,
        Instant occurredAt
) {
}
