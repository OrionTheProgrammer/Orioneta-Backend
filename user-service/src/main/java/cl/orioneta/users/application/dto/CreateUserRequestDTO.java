package cl.orioneta.users.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Solicitud usada para crear el perfil publico despues del registro.
 *
 * <p>Las credenciales de autenticacion pertenecen a {@code auth-service}. Este
 * DTO solo contiene campos de identidad publica administrados por
 * {@code user-service}.
 *
 * @param username username unico elegido por la persona
 * @param displayName nombre visible dentro de Orioneta
 * @param email correo usado para conectar auth con el perfil publico
 * @param bio biografia corta opcional
 */
public record CreateUserRequestDTO(
        @NotBlank(message = "El username es obligatorio")
        @Size(min = 3, max = 30, message = "El username debe tener entre 3 y 30 caracteres")
        String username,

        @NotBlank(message = "El nombre visible es obligatorio")
        @Size(min = 2, max = 60, message = "El nombre visible debe tener entre 2 y 60 caracteres")
        String displayName,

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email no tiene un formato valido")
        String email,

        @Size(max = 160, message = "La biografia no puede superar los 160 caracteres")
        String bio
) {
}
