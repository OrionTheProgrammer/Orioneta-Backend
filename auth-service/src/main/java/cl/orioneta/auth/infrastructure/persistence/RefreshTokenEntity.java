package cl.orioneta.auth.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidad JPA para refresh tokens opacos.
 */
@Entity
@Table(
        name = "refresh_tokens",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_refresh_tokens_hash", columnNames = "token_hash")
        }
)
public class RefreshTokenEntity {

    @Id
    private UUID id;

    @Column(name = "auth_user_id", nullable = false)
    private UUID authUserId;

    @Column(name = "token_hash", nullable = false, length = 128)
    private String tokenHash;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "revoked_at")
    private LocalDateTime revokedAt;

    protected RefreshTokenEntity() {
    }

    public RefreshTokenEntity(
            UUID id,
            UUID authUserId,
            String tokenHash,
            LocalDateTime createdAt,
            LocalDateTime expiresAt,
            LocalDateTime revokedAt
    ) {
        this.id = id;
        this.authUserId = authUserId;
        this.tokenHash = tokenHash;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.revokedAt = revokedAt;
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
}
