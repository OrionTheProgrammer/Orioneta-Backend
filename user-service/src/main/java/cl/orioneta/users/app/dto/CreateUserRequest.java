package cl.orioneta.users.app.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Datos que necesita Orioneta para crear el perfil publico de un usuario.
 */
public record CreateUserRequest(
        @NotBlank(message = "El username es obligatorio")
        @Size(min = 3, max = 60, message = "El username debe tener entre 3 y 60 caracteres")
        String userName,

        @NotBlank(message = "El nombre visible es obligatorio")
        @Size(min = 3, max = 60, message = "El nombre visible debe tener entre 3 y 60 caracteres")
        String displayName,

        @Size(max = 260, message = "La biografia no puede superar los 260 caracteres")
        String bio,

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email no tiene un formato valido")
        @Size(max = 120, message = "El email no puede superar los 120 caracteres")
        String email,

        @Size(max = 500, message = "La foto de perfil no puede superar los 500 caracteres")
        String profilePhoto
) {
}
