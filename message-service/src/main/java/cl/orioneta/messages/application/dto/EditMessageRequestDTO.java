package cl.orioneta.messages.application.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Datos para editar el contenido de un mensaje.
 */
public record EditMessageRequestDTO(
        @NotBlank(message = "El contenido es obligatorio")
        String content
) {
}
