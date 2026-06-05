package cl.orioneta.audit.infrastructure.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaAuditRepository extends JpaRepository<AuditEntity, UUID> {

    List<AuditEntity> findTop100ByOrderByOccurredAtDesc();

    List<AuditEntity> findByTargetTypeAndTargetIdOrderByOccurredAtDesc(String targetType, UUID targetId);
}
