package cl.orioneta.audit.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record AuditEventResponseDTO(
        UUID id,
        String sourceService,
        String action,
        String targetType,
        UUID targetId,
        UUID actorUserId,
        String detail,
        LocalDateTime occurredAt
) {
}
