package cl.orioneta.customization.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Preferencias visuales globales de un usuario.
 */
public class UserCustomization {

    private final UUID id;
    private final UUID userId;
    private String activeGlobalThemeId;
    private String activeFontId;
    private int animationLevel;
    private boolean compactMode;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private UserCustomization(UUID id, UUID userId, String activeGlobalThemeId, String activeFontId, int animationLevel, boolean compactMode, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = Objects.requireNonNull(id, "El id es obligatorio");
        this.userId = Objects.requireNonNull(userId, "El usuario es obligatorio");
        this.activeGlobalThemeId = normalize(activeGlobalThemeId);
        this.activeFontId = normalize(activeFontId);
        this.animationLevel = Math.max(0, Math.min(animationLevel, 5));
        this.compactMode = compactMode;
        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
        this.updatedAt = updatedAt == null ? this.createdAt : updatedAt;
    }

    public static UserCustomization createDefault(UUID userId) {
        return new UserCustomization(UUID.randomUUID(), userId, "default", "system", 3, false, LocalDateTime.now(), LocalDateTime.now());
    }

    public static UserCustomization rehydrate(UUID id, UUID userId, String activeGlobalThemeId, String activeFontId, int animationLevel, boolean compactMode, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new UserCustomization(id, userId, activeGlobalThemeId, activeFontId, animationLevel, compactMode, createdAt, updatedAt);
    }

    public void update(String activeGlobalThemeId, String activeFontId, Integer animationLevel, Boolean compactMode) {
        if (activeGlobalThemeId != null && !activeGlobalThemeId.isBlank()) {
            this.activeGlobalThemeId = activeGlobalThemeId.trim();
        }
        if (activeFontId != null && !activeFontId.isBlank()) {
            this.activeFontId = activeFontId.trim();
        }
        if (animationLevel != null) {
            this.animationLevel = Math.max(0, Math.min(animationLevel, 5));
        }
        if (compactMode != null) {
            this.compactMode = compactMode;
        }
        this.updatedAt = LocalDateTime.now();
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

    private static String normalize(String value) {
        return value == null || value.isBlank() ? "" : value.trim();
    }
}
