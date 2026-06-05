package cl.orioneta.audit.infrastructure.out.persistence;

import cl.orioneta.audit.domain.model.AuditEvent;
import cl.orioneta.audit.domain.repository.AuditRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class AuditPersistenceAdapter implements AuditRepositoryPort {

    private final JpaAuditRepository jpaAuditRepository;

    public AuditPersistenceAdapter(JpaAuditRepository jpaAuditRepository) {
        this.jpaAuditRepository = jpaAuditRepository;
    }

    @Override
    public AuditEvent save(AuditEvent auditEvent) {
        return toDomain(jpaAuditRepository.save(toEntity(auditEvent)));
    }

    @Override
    public List<AuditEvent> findByTarget(String targetType, UUID targetId) {
        return jpaAuditRepository.findByTargetTypeAndTargetIdOrderByOccurredAtDesc(targetType, targetId)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<AuditEvent> findRecent() {
        return jpaAuditRepository.findTop100ByOrderByOccurredAtDesc()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    private AuditEntity toEntity(AuditEvent event) {
        return new AuditEntity(
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

    private AuditEvent toDomain(AuditEntity entity) {
        return AuditEvent.rehydrate(
                entity.getId(),
                entity.getSourceService(),
                entity.getAction(),
                entity.getTargetType(),
                entity.getTargetId(),
                entity.getActorUserId(),
                entity.getDetail(),
                entity.getOccurredAt()
        );
    }
}
