package cl.orioneta.users.app.dto;

import cl.orioneta.users.domain.model.VisibilityStatus;
import jakarta.validation.constraints.NotNull;

/**
 * Cambio de visibilidad publica de la cuenta.
 */
public record UpdateUserVisibilityRequest(
        @NotNull(message = "La visibilidad es obligatoria")
        VisibilityStatus visibility
) {
}
