package cl.orioneta.auth.app.security;

import java.time.Instant;

/**
 * Access token JWT emitido por Orioneta.
 */
public record AccessToken(
        String value,
        Instant expiresAt,
        long expiresInSeconds
) {
}
