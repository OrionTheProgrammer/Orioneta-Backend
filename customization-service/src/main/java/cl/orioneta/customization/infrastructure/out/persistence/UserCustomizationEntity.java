package cl.orioneta.customization.infrastructure.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "user_customizations",
        uniqueConstraints = @UniqueConstraint(name = "uk_user_customizations_user", columnNames = "user_id")
)
public class UserCustomizationEntity {

    @Id
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "active_global_theme_id")
    private String activeGlobalThemeId;

    @Column(name = "active_font_id")
    private String activeFontId;

    @Column(name = "animation_level", nullable = false)
    private int animationLevel;

    @Column(name = "compact_mode", nullable = false)
    private boolean compactMode;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected UserCustomizationEntity() {
    }

    public UserCustomizationEntity(UUID id, UUID userId, String activeGlobalThemeId, String activeFontId, int animationLevel, boolean compactMode, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.activeGlobalThemeId = activeGlobalThemeId;
        this.activeFontId = activeFontId;
        this.animationLevel = animationLevel;
        this.compactMode = compactMode;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getActiveGlobalThemeId() {
        return activeGlobalThemeId;
    }

    public String getActiveFontId() {
        return activeFontId;
    }

    public int getAnimationLevel() {
        return animationLevel;
    }

    public boolean isCompactMode() {
        return compactMode;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
