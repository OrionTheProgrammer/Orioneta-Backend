package cl.orioneta.moderation.infrastructure.out.persistence;

import cl.orioneta.moderation.domain.model.ModerationStatus;
import cl.orioneta.moderation.domain.model.ModerationTargetType;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "moderation_reviews")
public class ModerationReviewEntity {

    @Id
    private UUID id;
    @Column(name = "target_id", nullable = false)
    private UUID targetId;
    @Enumerated(EnumType.STRING)
    @Column(name = "target_type", nullable = false, length = 40)
    private ModerationTargetType targetType;
    @Column(name = "reviewer_id")
    private UUID reviewerId;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ModerationStatus status;
    @Column(length = 1000)
    private String reason;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    protected ModerationReviewEntity() {
    }

    public ModerationReviewEntity(UUID id, UUID targetId, ModerationTargetType targetType, UUID reviewerId, ModerationStatus status, String reason, LocalDateTime createdAt, LocalDateTime resolvedAt) {
        this.id = id;
        this.targetId = targetId;
        this.targetType = targetType;
        this.reviewerId = reviewerId;
        this.status = status;
        this.reason = reason;
        this.createdAt = createdAt;
        this.resolvedAt = resolvedAt;
    }

    public UUID getId() { return id; }
    public UUID getTargetId() { return targetId; }
    public ModerationTargetType getTargetType() { return targetType; }
    public UUID getReviewerId() { return reviewerId; }
    public ModerationStatus getStatus() { return status; }
    public String getReason() { return reason; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getResolvedAt() { return resolvedAt; }
}
