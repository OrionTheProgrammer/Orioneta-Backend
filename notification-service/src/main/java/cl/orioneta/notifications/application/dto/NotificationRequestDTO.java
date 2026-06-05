package cl.orioneta.notifications.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * Datos para crear una notificacion.
 */
public record NotificationRequestDTO(
        @NotNull(message = "El usuario es obligatorio")
        UUID userId,

        @NotBlank(message = "El tipo es obligatorio")
        String type,

        @NotBlank(message = "El titulo es obligatorio")
        String title,

        String body
) {
}
