package cl.orioneta.audit.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Evento de auditoria registrado por Orioneta.
 */
public class AuditEvent {

    private final UUID id;
    private final String sourceService;
    private final String action;
    private final String targetType;
    private final UUID targetId;
    private final UUID actorUserId;
    private final String detail;
    private final LocalDateTime occurredAt;

    private AuditEvent(UUID id, String sourceService, String action, String targetType, UUID targetId, UUID actorUserId, String detail, LocalDateTime occurredAt) {
        this.id = Objects.requireNonNull(id, "El id es obligatorio");
        this.sourceService = requireText(sourceService, "El servicio origen es obligatorio");
        this.action = requireText(action, "La accion es obligatoria");
        this.targetType = requireText(targetType, "El tipo de entidad es obligatorio");
        this.targetId = targetId;
        this.actorUserId = actorUserId;
        this.detail = detail == null ? "" : detail.trim();
        this.occurredAt = occurredAt == null ? LocalDateTime.now() : occurredAt;
    }

    public static AuditEvent create(String sourceService, String action, String targetType, UUID targetId, UUID actorUserId, String detail) {
        return new AuditEvent(UUID.randomUUID(), sourceService, action, targetType, targetId, actorUserId, detail, LocalDateTime.now());
    }

    public static AuditEvent rehydrate(UUID id, String sourceService, String action, String targetType, UUID targetId, UUID actorUserId, String detail, LocalDateTime occurredAt) {
        return new AuditEvent(id, sourceService, action, targetType, targetId, actorUserId, detail, occurredAt);
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

    private static String requireText(String value, String message) {
        if (value == null || value.trim().isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
