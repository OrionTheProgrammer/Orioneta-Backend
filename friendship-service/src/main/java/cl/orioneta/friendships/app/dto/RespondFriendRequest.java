package cl.orioneta.friendships.app.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Datos para aceptar, rechazar o cancelar una solicitud.
 */
public record RespondFriendRequest(
        @NotNull(message = "El usuario que responde es obligatorio")
        UUID requesterUserId
) {
}
