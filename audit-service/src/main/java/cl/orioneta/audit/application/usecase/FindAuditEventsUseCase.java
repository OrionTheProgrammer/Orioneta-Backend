package cl.orioneta.audit.application.usecase;

import cl.orioneta.audit.domain.model.AuditEvent;
import cl.orioneta.audit.domain.repository.AuditRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FindAuditEventsUseCase {

    private final AuditRepositoryPort auditRepositoryPort;

    public FindAuditEventsUseCase(AuditRepositoryPort auditRepositoryPort) {
        this.auditRepositoryPort = auditRepositoryPort;
    }

    public List<AuditEvent> findRecent() {
        return auditRepositoryPort.findRecent();
    }

    public List<AuditEvent> findByTarget(String targetType, UUID targetId) {
        return auditRepositoryPort.findByTarget(targetType, targetId);
    }
}
