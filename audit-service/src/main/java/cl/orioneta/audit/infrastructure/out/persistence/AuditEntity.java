package cl.orioneta.audit.infrastructure.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "audit_events")
public class AuditEntity {

    @Id
    private UUID id;

    @Column(name = "source_service", nullable = false, length = 80)
    private String sourceService;

    @Column(nullable = false, length = 100)
    private String action;

    @Column(name = "target_type", nullable = false, length = 80)
    private String targetType;

    @Column(name = "target_id")
    private UUID targetId;

    @Column(name = "actor_user_id")
    private UUID actorUserId;

    @Column(length = 1000)
    private String detail;

    @Column(name = "occurred_at", nullable = false)
    private LocalDateTime occurredAt;

    protected AuditEntity() {
    }

    public AuditEntity(UUID id, String sourceService, String action, String targetType, UUID targetId, UUID actorUserId, String detail, LocalDateTime occurredAt) {
        this.id = id;
        this.sourceService = sourceService;
        this.action = action;
        this.targetType = targetType;
        this.targetId = targetId;
        this.actorUserId = actorUserId;
        this.detail = detail;
        this.occurredAt = occurredAt;
    }

    public UUID getId() {
        return id;
    }

    public String getSourceService() {
        return sourceService;
    }

    public String getAction() {
        return action;
    }

    public String getTargetType() {
        return targetType;
    }

    public UUID getTargetId() {
        return targetId;
    }

    public UUID getActorUserId() {
        return actorUserId;
    }

    public String getDetail() {
        return detail;
    }

    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }
}
