package cl.orioneta.users.infrastructure.persistence;

import cl.orioneta.users.domain.model.Status;
import cl.orioneta.users.domain.model.VisibilityStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidad JPA que representa la tabla de usuarios.
 *
 * <p>No contiene reglas de negocio. Su trabajo es traducir el estado del
 * dominio a una forma que PostgreSQL pueda guardar.</p>
 */
@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_users_user_name", columnNames = "user_name"),
                @UniqueConstraint(name = "uk_users_email", columnNames = "email"),
                @UniqueConstraint(name = "uk_users_friend_code", columnNames = "friend_code")
        }
)
public class UserEntity {

    @Id
    @Column(name = "user_id", nullable = false)
    private UUID userID;

    @Column(name = "user_name", nullable = false, length = 60)
    private String userName;

    @Column(name = "display_name", nullable = false, length = 60)
    private String displayName;

    @Column(length = 260)
    private String bio;

    @Column(nullable = false, length = 120)
    private String email;

    @Column(name = "friend_code", nullable = false, length = 12)
    private String friendCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private VisibilityStatus visibility;

    @Column(name = "profile_photo", length = 500)
    private String profilePhoto;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected UserEntity() {
    }

    public UserEntity(
            UUID userID,
            String userName,
            String displayName,
            String bio,
            String email,
            String friendCode,
            Status status,
            VisibilityStatus visibility,
            String profilePhoto,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.userID = userID;
        this.userName = userName;
        this.displayName = displayName;
        this.bio = bio;
        this.email = email;
        this.friendCode = friendCode;
        this.status = status;
        this.visibility = visibility;
        this.profilePhoto = profilePhoto;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @PrePersist
    void prePersist() {
        LocalDateTime now = LocalDateTime.now();

        if (createdAt == null) {
            createdAt = now;
        }

        if (updatedAt == null) {
            updatedAt = createdAt;
        }
    }

    @PreUpdate
    void preUpdate() {
        if (updatedAt == null) {
            updatedAt = LocalDateTime.now();
        }
    }

    public UUID getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getBio() {
        return bio;
    }

    public String getEmail() {
        return email;
    }

    public String getFriendCode() {
        return friendCode;
    }

    public Status getStatus() {
        return status;
    }

    public VisibilityStatus getVisibility() {
        return visibility;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
