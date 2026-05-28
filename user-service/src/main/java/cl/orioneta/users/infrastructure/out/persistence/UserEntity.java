package cl.orioneta.users.infrastructure.out.persistence;

import cl.orioneta.users.domain.model.AccountVisibility;
import cl.orioneta.users.domain.model.UserStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidad JPA que representa la tabla de usuarios.
 *
 * <p>Esta clase pertenece a infraestructura. No debe contener reglas de negocio:
 * su responsabilidad es expresar columnas, restricciones e indices para
 * persistir el modelo {@code User}.
 */
@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_users_username", columnNames = "username"),
                @UniqueConstraint(name = "uk_users_email", columnNames = "email"),
                @UniqueConstraint(name = "uk_users_friend_code", columnNames = "friend_code")
        }
)
public class UserEntity {

    @Id
    private UUID id;

    @Column(nullable = false, length = 30)
    private String username;

    @Column(name = "display_name", nullable = false, length = 60)
    private String displayName;

    @Column(nullable = false, length = 120)
    private String email;

    @Column(name = "friend_code", nullable = false, length = 8)
    private String friendCode;

    @Column(name = "avatar_url", length = 512)
    private String avatarUrl;

    @Column(name = "banner_url", length = 512)
    private String bannerUrl;

    @Column(length = 160)
    private String bio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_visibility", nullable = false, length = 20)
    private AccountVisibility accountVisibility;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Constructor requerido por JPA.
     */
    protected UserEntity() {
    }

    /**
     * Crea una entidad a partir de todos sus campos persistibles.
     *
     * @param id id del usuario
     * @param username username unico
     * @param displayName nombre visible
     * @param email correo unico
     * @param friendCode codigo unico de amistad
     * @param avatarUrl URL de avatar
     * @param bannerUrl URL de banner
     * @param bio biografia publica
     * @param status estado de presencia
     * @param accountVisibility visibilidad social
     * @param createdAt fecha de creacion
     * @param updatedAt fecha de ultima actualizacion
     */
    public UserEntity(
            UUID id,
            String username,
            String displayName,
            String email,
            String friendCode,
            String avatarUrl,
            String bannerUrl,
            String bio,
            UserStatus status,
            AccountVisibility accountVisibility,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.username = username;
        this.displayName = displayName;
        this.email = email;
        this.friendCode = friendCode;
        this.avatarUrl = avatarUrl;
        this.bannerUrl = bannerUrl;
        this.bio = bio;
        this.status = status;
        this.accountVisibility = accountVisibility;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }

    public String getFriendCode() {
        return friendCode;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public String getBio() {
        return bio;
    }

    public UserStatus getStatus() {
        return status;
    }

    public AccountVisibility getAccountVisibility() {
        return accountVisibility;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
