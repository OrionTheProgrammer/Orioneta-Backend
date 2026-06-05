package cl.orioneta.conversations.infrastructure.out.persistence;

import cl.orioneta.conversations.domain.model.ConversationType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entidad JPA de conversacion.
 */
@Entity
@Table(name = "conversations")
public class ConversationEntity {

    @Id
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ConversationType type;

    @Column(length = 80)
    private String name;

    @Column(length = 240)
    private String description;

    @Column(name = "owner_id")
    private UUID ownerId;

    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;

    @Column(name = "background_url", length = 500)
    private String backgroundUrl;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "conversation_id")
    private List<ParticipantEntity> participants = new ArrayList<>();

    protected ConversationEntity() {
    }

    public ConversationEntity(
            UUID id,
            ConversationType type,
            String name,
            String description,
            UUID ownerId,
            String avatarUrl,
            String backgroundUrl,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            LocalDateTime deletedAt,
            List<ParticipantEntity> participants
    ) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.description = description;
        this.ownerId = ownerId;
        this.avatarUrl = avatarUrl;
        this.backgroundUrl = backgroundUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.participants = new ArrayList<>(participants);
    }

    public UUID getId() {
        return id;
    }

    public ConversationType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getBackgroundUrl() {
        return backgroundUrl;
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

    public List<ParticipantEntity> getParticipants() {
        return participants;
    }
}
