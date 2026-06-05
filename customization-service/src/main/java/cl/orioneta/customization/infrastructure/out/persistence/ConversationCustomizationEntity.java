package cl.orioneta.customization.infrastructure.out.persistence;

import cl.orioneta.customization.domain.model.BubbleStyle;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "conversation_customizations",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_conversation_customizations_user",
                columnNames = {"conversation_id", "user_id"}
        )
)
public class ConversationCustomizationEntity {

    @Id
    private UUID id;

    @Column(name = "conversation_id", nullable = false)
    private UUID conversationId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "active_chat_theme_id")
    private String activeChatThemeId;

    @Column(name = "active_background_id")
    private String activeBackgroundId;

    @Enumerated(EnumType.STRING)
    @Column(name = "bubble_style", nullable = false, length = 30)
    private BubbleStyle bubbleStyle;

    @Column(name = "font_size", nullable = false)
    private int fontSize;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected ConversationCustomizationEntity() {
    }

    public ConversationCustomizationEntity(UUID id, UUID conversationId, UUID userId, String activeChatThemeId, String activeBackgroundId, BubbleStyle bubbleStyle, int fontSize, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.conversationId = conversationId;
        this.userId = userId;
        this.activeChatThemeId = activeChatThemeId;
        this.activeBackgroundId = activeBackgroundId;
        this.bubbleStyle = bubbleStyle;
        this.fontSize = fontSize;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public UUID getConversationId() {
        return conversationId;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getActiveChatThemeId() {
        return activeChatThemeId;
    }

    public String getActiveBackgroundId() {
        return activeBackgroundId;
    }

    public BubbleStyle getBubbleStyle() {
        return bubbleStyle;
    }

    public int getFontSize() {
        return fontSize;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
