package cl.orioneta.auth.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Token opaco de refresco.
 *
 * <p>El token real nunca se guarda: solo su hash. Si la base de datos se filtra,
 * el atacante no obtiene refresh tokens reutilizables.</p>
 */
public class RefreshToken {

    private final UUID id;
    private final UUID authUserId;
    private final String tokenHash;
    private final LocalDateTime createdAt;
    private final LocalDateTime expiresAt;
    private LocalDateTime revokedAt;

    private RefreshToken(
            UUID id,
            UUID authUserId,
            String tokenHash,
            LocalDateTime createdAt,
            LocalDateTime expiresAt,
            LocalDateTime revokedAt
    ) {
        this.id = Objects.requireNonNull(id, "El id del refresh token es obligatorio");
        this.authUserId = Objects.requireNonNull(authUserId, "El usuario del refresh token es obligatorio");
        this.tokenHash = requireText(tokenHash, "El hash del refresh token es obligatorio");
        this.createdAt = Objects.requireNonNull(createdAt, "La fecha de creacion es obligatoria");
        this.expiresAt = Objects.requireNonNull(expiresAt, "La fecha de expiracion es obligatoria");
        this.revokedAt = revokedAt;
    }

    public static RefreshToken create(UUID authUserId, String tokenHash, LocalDateTime expiresAt) {
        return new RefreshToken(UUID.randomUUID(), authUserId, tokenHash, LocalDateTime.now(), expiresAt, null);
    }

    public static RefreshToken rehydrate(
            UUID id,
            UUID authUserId,
            String tokenHash,
            LocalDateTime createdAt,
            LocalDateTime expiresAt,
            LocalDateTime revokedAt
    ) {
        return new RefreshToken(id, authUserId, tokenHash, createdAt, expiresAt, revokedAt);
    }

    public void revoke() {
        if (revokedAt == null) {
            revokedAt = LocalDateTime.now();
        }
    }

    public boolean isActive() {
        return revokedAt == null && expiresAt.isAfter(LocalDateTime.now());
    }

    public UUID getId() {
        return id;
    }

    public UUID getAuthUserId() {
        return authUserId;
    }

    public String getTokenHash() {
        return tokenHash;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public LocalDateTime getRevokedAt() {
        return revokedAt;
    }

    private static String requireText(String value, String message) {
        if (value == null || value.trim().isBlank()) {
            throw new IllegalArgumentException(message);
        }

        return value.trim();
    }
}
