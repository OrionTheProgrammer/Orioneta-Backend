package cl.orioneta.users.app.dto;

import jakarta.validation.constraints.Size;

/**
 * Datos editables del perfil publico.
 *
 * <p>Los campos pueden venir en {@code null}; eso significa que ese dato no se
 * cambia.</p>
 */
public record UpdateUserProfileRequest(
        @Size(min = 3, max = 60, message = "El nombre visible debe tener entre 3 y 60 caracteres")
        String displayName,

        @Size(max = 260, message = "La biografia no puede superar los 260 caracteres")
        String bio,

        @Size(max = 500, message = "La foto de perfil no puede superar los 500 caracteres")
        String profilePhoto
) {
}
