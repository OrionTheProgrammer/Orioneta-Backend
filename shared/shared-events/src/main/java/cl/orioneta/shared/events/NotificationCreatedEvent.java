package cl.orioneta.shared.events;

import java.time.Instant;
import java.util.UUID;

public record NotificationCreatedEvent(
        UUID notificationId,
        UUID userId,
        String type,
        Instant occurredAt
) {
}
