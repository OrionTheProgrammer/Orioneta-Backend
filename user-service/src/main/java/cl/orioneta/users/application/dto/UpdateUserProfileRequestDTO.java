package cl.orioneta.users.application.dto;

import jakarta.validation.constraints.Size;

/**
 * Solicitud para actualizar los datos publicos editables del perfil.
 *
 * <p>No permite cambiar username, email ni friend code porque esos campos
 * participan en autenticacion, busqueda y amistad. Cualquier cambio futuro en
 * ellos deberia tener un caso de uso propio.
 *
 * @param displayName nuevo nombre visible, opcional
 * @param bio nueva biografia, opcional
 * @param avatarUrl nueva URL de avatar, opcional
 * @param bannerUrl nueva URL de banner, opcional
 */
public record UpdateUserProfileRequestDTO(
        @Size(min = 2, max = 60, message = "El nombre visible debe tener entre 2 y 60 caracteres")
        String displayName,

        @Size(max = 160, message = "La biografia no puede superar los 160 caracteres")
        String bio,

        String avatarUrl,
        String bannerUrl
) {
}
