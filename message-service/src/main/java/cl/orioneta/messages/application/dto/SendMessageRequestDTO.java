package cl.orioneta.messages.application.dto;

import cl.orioneta.messages.domain.model.MessageType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * Datos para enviar un mensaje.
 */
public record SendMessageRequestDTO(
        @NotNull(message = "La conversacion es obligatoria")
        UUID conversationId,

        @NotNull(message = "El emisor es obligatorio")
        UUID senderId,

        @NotBlank(message = "El contenido es obligatorio")
        String content,

        MessageType type
) {
}
