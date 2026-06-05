package cl.orioneta.notifications.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Notificacion interna de Orioneta.
 */
public class Notification {

    private final UUID id;
    private final UUID userId;
    private String type;
    private String title;
    private String body;
    private boolean read;
    private final LocalDateTime createdAt;
    private LocalDateTime readAt;

    private Notification(
            UUID id,
            UUID userId,
            String type,
            String title,
            String body,
            boolean read,
            LocalDateTime createdAt,
            LocalDateTime readAt
    ) {
        this.id = Objects.requireNonNull(id, "El id de notificacion es obligatorio");
        this.userId = Objects.requireNonNull(userId, "El usuario es obligatorio");
        this.type = requireText(type, "El tipo de notificacion es obligatorio");
        this.title = requireText(title, "El titulo es obligatorio");
        this.body = body == null ? "" : body.trim();
        this.read = read;
        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
        this.readAt = readAt;
    }

    public static Notification create(UUID userId, String type, String title, String body) {
        return new Notification(UUID.randomUUID(), userId, type, title, body, false, LocalDateTime.now(), null);
    }

    public static Notification rehydrate(
            UUID id,
            UUID userId,
            String type,
            String title,
            String body,
            boolean read,
            LocalDateTime createdAt,
            LocalDateTime readAt
    ) {
        return new Notification(id, userId, type, title, body, read, createdAt, readAt);
    }

    public void markAsRead() {
        if (!read) {
            read = true;
            readAt = LocalDateTime.now();
        }
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public boolean isRead() {
        return read;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getReadAt() {
        return readAt;
    }

    private static String requireText(String value, String message) {
        if (value == null || value.trim().isBlank()) {
            throw new IllegalArgumentException(message);
        }

        return value.trim();
    }
}
