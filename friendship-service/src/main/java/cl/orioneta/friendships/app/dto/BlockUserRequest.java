package cl.orioneta.friendships.app.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Datos necesarios para bloquear a otro usuario.
 */
public record BlockUserRequest(
        @NotNull(message = "El usuario que bloquea es obligatorio")
        UUID userId,

        @NotNull(message = "El usuario bloqueado es obligatorio")
        UUID blockedUserId
) {
}
