package cl.orioneta.auth.app.dto;

import cl.orioneta.auth.domain.model.Role;
import java.time.Instant;
import java.util.UUID;

/**
 * Resultado de validar un access token de Orioneta.
 */
public record TokenValidationResponse(
        boolean valid,
        UUID userId,
        String email,
        Role role,
        Instant expiresAt
) {
}
