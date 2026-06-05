package cl.orioneta.users.app.dto;

import cl.orioneta.users.domain.model.Status;
import jakarta.validation.constraints.NotNull;

/**
 * Cambio del estado de presencia de un usuario.
 */
public record UpdateUserStatusRequest(
        @NotNull(message = "El estado es obligatorio")
        Status status
) {
}
