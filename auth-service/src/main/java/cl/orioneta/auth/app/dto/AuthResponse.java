package cl.orioneta.auth.app.dto;

import cl.orioneta.auth.domain.model.Role;
import java.util.UUID;

/**
 * Respuesta de autenticacion emitida por Orioneta.
 */
public record AuthResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        long expiresInSeconds,
        UUID userId,
        String email,
        Role role
) {
}
