package cl.orioneta.messages.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Mensaje enviado dentro de una conversacion.
 */
public class Message {

    private final UUID id;
    private final UUID conversationId;
    private final UUID senderId;
    private String content;
    private MessageType type;
    private MessageStatus status;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    private Message(
            UUID id,
            UUID conversationId,
            UUID senderId,
            String content,
            MessageType type,
            MessageStatus status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            LocalDateTime deletedAt
    ) {
        this.id = Objects.requireNonNull(id, "El id del mensaje es obligatorio");
        this.conversationId = Objects.requireNonNull(conversationId, "La conversacion es obligatoria");
        this.senderId = Objects.requireNonNull(senderId, "El emisor es obligatorio");
        this.content = requireText(content, "El contenido del mensaje es obligatorio");
        this.type = type == null ? MessageType.TEXT : type;
        this.status = status == null ? MessageStatus.SENT : status;
        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
        this.updatedAt = updatedAt == null ? this.createdAt : updatedAt;
        this.deletedAt = deletedAt;
    }

    public static Message create(UUID conversationId, UUID senderId, String content, MessageType type) {
        LocalDateTime now = LocalDateTime.now();
        return new Message(UUID.randomUUID(), conversationId, senderId, content, type, MessageStatus.SENT, now, now, null);
    }

    public static Message rehydrate(
            UUID id,
            UUID conversationId,
            UUID senderId,
            String content,
            MessageType type,
            MessageStatus status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            LocalDateTime deletedAt
    ) {
        return new Message(id, conversationId, senderId, content, type, status, createdAt, updatedAt, deletedAt);
    }

    public void edit(String content) {
        if (deletedAt != null) {
            throw new IllegalStateException("No se puede editar un mensaje eliminado");
        }

        this.content = requireText(content, "El contenido del mensaje es obligatorio");
        touch();
    }

    public void markAsRead() {
        if (status != MessageStatus.DELETED) {
            status = MessageStatus.READ;
            touch();
        }
    }

    public void delete() {
        status = MessageStatus.DELETED;
        deletedAt = LocalDateTime.now();
        touch();
    }

    public UUID getId() {
        return id;
    }

    public UUID getConversationId() {
        return conversationId;
    }

    public UUID getSenderId() {
        return senderId;
    }

    public String getContent() {
        return content;
    }

    public MessageType getType() {
        return type;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    private void touch() {
        updatedAt = LocalDateTime.now();
    }

    private static String requireText(String value, String message) {
        if (value == null || value.trim().isBlank()) {
            throw new IllegalArgumentException(message);
        }

        return value.trim();
    }
}
