package cl.orioneta.customization.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Preferencias visuales de un usuario para una conversacion.
 */
public class ConversationCustomization {

    private final UUID id;
    private final UUID conversationId;
    private final UUID userId;
    private String activeChatThemeId;
    private String activeBackgroundId;
    private BubbleStyle bubbleStyle;
    private int fontSize;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private ConversationCustomization(UUID id, UUID conversationId, UUID userId, String activeChatThemeId, String activeBackgroundId, BubbleStyle bubbleStyle, int fontSize, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = Objects.requireNonNull(id, "El id es obligatorio");
        this.conversationId = Objects.requireNonNull(conversationId, "La conversacion es obligatoria");
        this.userId = Objects.requireNonNull(userId, "El usuario es obligatorio");
        this.activeChatThemeId = normalize(activeChatThemeId);
        this.activeBackgroundId = normalize(activeBackgroundId);
        this.bubbleStyle = bubbleStyle == null ? BubbleStyle.DEFAULT : bubbleStyle;
        this.fontSize = fontSize <= 0 ? 16 : fontSize;
        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
        this.updatedAt = updatedAt == null ? this.createdAt : updatedAt;
    }

    public static ConversationCustomization createDefault(UUID conversationId, UUID userId) {
        return new ConversationCustomization(UUID.randomUUID(), conversationId, userId, "default", "", BubbleStyle.DEFAULT, 16, LocalDateTime.now(), LocalDateTime.now());
    }

    public static ConversationCustomization rehydrate(UUID id, UUID conversationId, UUID userId, String activeChatThemeId, String activeBackgroundId, BubbleStyle bubbleStyle, int fontSize, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new ConversationCustomization(id, conversationId, userId, activeChatThemeId, activeBackgroundId, bubbleStyle, fontSize, createdAt, updatedAt);
    }

    public void update(String activeChatThemeId, String activeBackgroundId, BubbleStyle bubbleStyle, Integer fontSize) {
        if (activeChatThemeId != null && !activeChatThemeId.isBlank()) {
            this.activeChatThemeId = activeChatThemeId.trim();
        }
        if (activeBackgroundId != null) {
            this.activeBackgroundId = activeBackgroundId.trim();
        }
        if (bubbleStyle != null) {
            this.bubbleStyle = bubbleStyle;
        }
        if (fontSize != null && fontSize > 0) {
            this.fontSize = fontSize;
        }
        this.updatedAt = LocalDateTime.now();
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

    private static String normalize(String value) {
        return value == null || value.isBlank() ? "" : value.trim();
    }
}
