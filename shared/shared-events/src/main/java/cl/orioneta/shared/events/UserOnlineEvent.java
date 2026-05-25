package cl.orioneta.shared.events;

import java.time.Instant;
import java.util.UUID;

public record UserOnlineEvent(
        UUID userId,
        Instant occurredAt
) {
}
