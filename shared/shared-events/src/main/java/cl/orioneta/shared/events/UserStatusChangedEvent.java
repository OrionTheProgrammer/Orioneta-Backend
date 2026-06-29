package cl.orioneta.shared.events;

import java.time.Instant;
import java.util.UUID;

public record UserStatusChangedEvent(
        UUID userId,
        String type,
        String newValue,
        String oldValue,
        Instant occurredAt
) {
}
