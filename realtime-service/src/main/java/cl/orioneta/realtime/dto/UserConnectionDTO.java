package cl.orioneta.realtime.dto;

import java.time.Instant;
import java.util.UUID;

public record UserConnectionDTO(
        UUID userId,
        String sessionId,
        Instant connectedAt
) {
}
