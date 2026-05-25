package cl.orioneta.shared.security;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record JwtClaims(
        UUID userId,
        String email,
        Set<String> roles,
        Instant expiresAt
) {
}
