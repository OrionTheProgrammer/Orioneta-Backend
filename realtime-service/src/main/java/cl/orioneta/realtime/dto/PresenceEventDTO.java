package cl.orioneta.realtime.dto;

import java.time.Instant;
import java.util.UUID;

public record PresenceEventDTO(
        String type,
        UUID userId,
        boolean online,
        Instant occurredAt
) {
    public static PresenceEventDTO connected(UUID userId) {
        return new PresenceEventDTO("USER_CONNECTED", userId, true, Instant.now());
    }

    public static PresenceEventDTO disconnected(UUID userId) {
        return new PresenceEventDTO("USER_DISCONNECTED", userId, false, Instant.now());
    }
}
