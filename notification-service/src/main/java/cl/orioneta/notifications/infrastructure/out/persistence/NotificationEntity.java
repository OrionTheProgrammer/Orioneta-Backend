package cl.orioneta.notifications.infrastructure.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notifications")
public class NotificationEntity {

    @Id
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(nullable = false, length = 80)
    private String type;

    @Column(nullable = false, length = 160)
    private String title;

    @Column(length = 500)
    private String body;

    @Column(nullable = false)
    private boolean read;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "read_at")
    private LocalDateTime readAt;

    @Column(name = "sender_id")
    private UUID senderId;

    @Column(name = "sender_name", length = 120)
    private String senderName;

    @Column(name = "sender_avatar", length = 500)
    private String senderAvatar;

    @Column(name = "conversation_id")
    private UUID conversationId;

    protected NotificationEntity() {
    }

    public NotificationEntity(UUID id, UUID userId, String type, String title, String body, boolean read, LocalDateTime createdAt, LocalDateTime readAt, UUID senderId, String senderName, String senderAvatar, UUID conversationId) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.title = title;
        this.body = body;
        this.read = read;
        this.createdAt = createdAt;
        this.readAt = readAt;
        this.senderId = senderId;
        this.senderName = senderName;
        this.senderAvatar = senderAvatar;
        this.conversationId = conversationId;
    }

    public UUID getId() { return id; }
    public UUID getUserId() { return userId; }
    public String getType() { return type; }
    public String getTitle() { return title; }
    public String getBody() { return body; }
    public boolean isRead() { return read; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getReadAt() { return readAt; }
    public UUID getSenderId() { return senderId; }
    public String getSenderName() { return senderName; }
    public String getSenderAvatar() { return senderAvatar; }
    public UUID getConversationId() { return conversationId; }
}
