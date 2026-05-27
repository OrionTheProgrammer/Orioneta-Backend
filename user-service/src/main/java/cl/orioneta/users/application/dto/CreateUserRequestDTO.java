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
 * @param avatarUrl URL opcional de la imagen de avatar
 * @param bannerUrl URL opcional del banner del perfil
 * @param bio biografia corta opcional
 */
public record CreateUserRequestDTO(
        @NotBlank
        @Size(min = 3, max = 25)
        String username,

        @NotBlank
        @Size(min = 2, max = 30)
        String displayName,

        @NotBlank
        @Email
        String email,

        String avatarUrl,
        String bannerUrl,

        @Size(max = 160)
        String bio
) {
}
