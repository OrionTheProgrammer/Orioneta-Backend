package cl.orioneta.audit.application.mapper;

import cl.orioneta.audit.application.dto.AuditEventResponseDTO;
import cl.orioneta.audit.domain.model.AuditEvent;
import org.springframework.stereotype.Component;

@Component
public class AuditMapper {

    public AuditEventResponseDTO toResponse(AuditEvent event) {
        return new AuditEventResponseDTO(
                event.getId(),
                event.getSourceService(),
                event.getAction(),
                event.getTargetType(),
                event.getTargetId(),
                event.getActorUserId(),
                event.getDetail(),
                event.getOccurredAt()
        );
    }
}
