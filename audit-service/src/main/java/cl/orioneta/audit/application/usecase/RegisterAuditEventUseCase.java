package cl.orioneta.audit.application.usecase;

import cl.orioneta.audit.application.dto.AuditEventRequestDTO;
import cl.orioneta.audit.domain.model.AuditEvent;
import cl.orioneta.audit.domain.repository.AuditRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class RegisterAuditEventUseCase {

    private final AuditRepositoryPort auditRepositoryPort;

    public RegisterAuditEventUseCase(AuditRepositoryPort auditRepositoryPort) {
        this.auditRepositoryPort = auditRepositoryPort;
    }

    public AuditEvent execute(AuditEventRequestDTO request) {
        return auditRepositoryPort.save(AuditEvent.create(
                request.sourceService(),
                request.action(),
                request.targetType(),
                request.targetId(),
                request.actorUserId(),
                request.detail()
        ));
    }
}
