package cl.orioneta.users.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Solicitud usada para actualizar campos publicos editables del perfil.
 *
 * <p>Los campos {@code username}, {@code email} y {@code friendCode} no
 * aparecen aqui porque son datos de identidad con reglas de consistencia mas
 * fuertes. Si Orioneta permite cambiarlos mas adelante, deberian tener casos de
 * uso dedicados.
 *
 * @param displayName nuevo nombre visible
 * @param avatarUrl URL opcional de la imagen de avatar
 * @param bannerUrl URL opcional del banner del perfil
 * @param bio biografia corta opcional
 */
public record UpdateUserRequestDTO(
        @NotBlank
        @Size(min = 2, max = 30)
        String displayName,

        String avatarUrl,
        String bannerUrl,

        @Size(max = 160)
        String bio
) {
}
