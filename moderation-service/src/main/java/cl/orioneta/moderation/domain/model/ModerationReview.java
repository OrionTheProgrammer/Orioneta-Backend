package cl.orioneta.moderation.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Revision o reporte de moderacion.
 */
public class ModerationReview {

    private final UUID id;
    private final UUID targetId;
    private final ModerationTargetType targetType;
    private UUID reviewerId;
    private ModerationStatus status;
    private String reason;
    private final LocalDateTime createdAt;
    private LocalDateTime resolvedAt;

    private ModerationReview(UUID id, UUID targetId, ModerationTargetType targetType, UUID reviewerId, ModerationStatus status, String reason, LocalDateTime createdAt, LocalDateTime resolvedAt) {
        this.id = Objects.requireNonNull(id, "El id es obligatorio");
        this.targetId = Objects.requireNonNull(targetId, "El objetivo es obligatorio");
        this.targetType = Objects.requireNonNull(targetType, "El tipo de objetivo es obligatorio");
        this.reviewerId = reviewerId;
        this.status = status == null ? ModerationStatus.PENDING : status;
        this.reason = reason == null ? "" : reason.trim();
        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
        this.resolvedAt = resolvedAt;
    }

    public static ModerationReview create(UUID targetId, ModerationTargetType targetType, String reason) {
        return new ModerationReview(UUID.randomUUID(), targetId, targetType, null, ModerationStatus.PENDING, reason, LocalDateTime.now(), null);
    }

    public static ModerationReview rehydrate(UUID id, UUID targetId, ModerationTargetType targetType, UUID reviewerId, ModerationStatus status, String reason, LocalDateTime createdAt, LocalDateTime resolvedAt) {
        return new ModerationReview(id, targetId, targetType, reviewerId, status, reason, createdAt, resolvedAt);
    }

    public void resolve(UUID reviewerId, ModerationStatus status, String reason) {
        if (status == ModerationStatus.PENDING) {
            throw new IllegalArgumentException("La resolucion no puede quedar pendiente");
        }
        this.reviewerId = Objects.requireNonNull(reviewerId, "El revisor es obligatorio");
        this.status = Objects.requireNonNull(status, "El estado es obligatorio");
        this.reason = reason == null ? "" : reason.trim();
        this.resolvedAt = LocalDateTime.now();
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
