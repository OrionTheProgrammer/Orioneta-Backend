package cl.orioneta.audit.domain.repository;

import cl.orioneta.audit.domain.model.AuditEvent;

import java.util.List;
import java.util.UUID;

public interface AuditRepositoryPort {

    AuditEvent save(AuditEvent auditEvent);

    List<AuditEvent> findByTarget(String targetType, UUID targetId);

    List<AuditEvent> findRecent();
}
