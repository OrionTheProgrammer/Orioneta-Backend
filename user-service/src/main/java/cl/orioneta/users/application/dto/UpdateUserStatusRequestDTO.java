package cl.orioneta.users.application.dto;

import cl.orioneta.users.domain.model.UserStatus;
import jakarta.validation.constraints.NotNull;

/**
 * Solicitud para cambiar el estado de presencia del usuario.
 *
 * <p>El estado se mantiene dentro de {@code user-service}; la propagacion en
 * tiempo real queda para casos de uso futuros integrados con eventos y
 * {@code realtime-service}.
 *
 * @param status nuevo estado de presencia
 */
public record UpdateUserStatusRequestDTO(
        @NotNull(message = "El estado es obligatorio")
        UserStatus status
) {
}
