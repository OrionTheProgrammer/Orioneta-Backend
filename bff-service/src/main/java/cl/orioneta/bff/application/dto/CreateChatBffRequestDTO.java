package cl.orioneta.bff.application.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

/**
 * Solicitud del frontend para crear una conversacion privada o grupal.
 */
public record CreateChatBffRequestDTO(
        @NotNull(message = "El tipo de conversacion es obligatorio")
        String type,

        @Size(max = 80, message = "El nombre no puede superar los 80 caracteres")
        String name,

        @Size(max = 240, message = "La descripcion no puede superar los 240 caracteres")
        String description,

        UUID ownerId,

        @NotNull(message = "Los participantes son obligatorios")
        @Size(min = 1, message = "Debes indicar al menos un participante")
        List<UUID> participantIds
) {
}
