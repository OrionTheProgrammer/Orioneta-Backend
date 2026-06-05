package cl.orioneta.audit.application.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record AuditEventRequestDTO(
        @NotBlank(message = "El servicio origen es obligatorio")
        String sourceService,

        @NotBlank(message = "La accion es obligatoria")
        String action,

        @NotBlank(message = "El tipo de entidad es obligatorio")
        String targetType,

        UUID targetId,
        UUID actorUserId,
        String detail
) {
}
