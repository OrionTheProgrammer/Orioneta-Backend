package cl.orioneta.shared.events;

import java.time.Instant;
import java.util.UUID;

public record MessageReadEvent(
        UUID messageId,
        UUID readerId,
        Instant occurredAt
) {
}
