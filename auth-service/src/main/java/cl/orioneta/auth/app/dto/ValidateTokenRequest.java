package cl.orioneta.auth.app.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Solicitud para validar un access token emitido por Orioneta.
 */
public record ValidateTokenRequest(
        @NotBlank(message = "El access token es obligatorio")
        String accessToken
) {
}
