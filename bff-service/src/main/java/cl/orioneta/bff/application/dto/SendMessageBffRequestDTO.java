package cl.orioneta.bff.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * Solicitud del frontend para enviar un mensaje desde la vista de chat.
 */
public record SendMessageBffRequestDTO(
        @NotNull(message = "La conversacion es obligatoria")
        UUID conversationId,

        @NotNull(message = "El emisor es obligatorio")
        UUID senderId,

        @NotBlank(message = "El contenido es obligatorio")
        String content,

        String type
) {
}
