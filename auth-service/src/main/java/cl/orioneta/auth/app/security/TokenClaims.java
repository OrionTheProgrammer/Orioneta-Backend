package cl.orioneta.auth.app.security;

import cl.orioneta.auth.domain.model.Role;
import java.time.Instant;
import java.util.UUID;

/**
 * Claims principales extraidos de un JWT de Orioneta.
 */
public record TokenClaims(
        UUID userId,
        String email,
        Role role,
        Instant expiresAt
) {
}
