package cl.orioneta.auth.app.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Solicitud de renovacion de sesion.
 */
public record RefreshTokenRequest(
        @NotBlank(message = "El refresh token es obligatorio")
        String refreshToken
) {
}
