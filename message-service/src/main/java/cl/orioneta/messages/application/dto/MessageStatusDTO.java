package cl.orioneta.messages.application.dto;

import cl.orioneta.messages.domain.model.MessageStatus;

/**
 * Respuesta simple para cambios de estado.
 */
public record MessageStatusDTO(
        MessageStatus status
) {
}
