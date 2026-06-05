package cl.orioneta.auth.app.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Datos para crear una cuenta local con email y password.
 */
public record RegisterRequest(
        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email no tiene un formato valido")
        @Size(max = 120, message = "El email no puede superar los 120 caracteres")
        String email,

        @NotBlank(message = "El password es obligatorio")
        @Size(min = 8, max = 120, message = "El password debe tener entre 8 y 120 caracteres")
        String password,

        @NotBlank(message = "El nombre visible es obligatorio")
        @Size(max = 80, message = "El nombre visible no puede superar los 80 caracteres")
        String displayName
) {
}
