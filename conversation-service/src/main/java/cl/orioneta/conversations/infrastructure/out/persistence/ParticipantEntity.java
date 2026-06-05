package cl.orioneta.conversations.infrastructure.out.persistence;

import cl.orioneta.conversations.domain.model.ParticipantRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidad JPA de participante.
 */
@Entity
@Table(name = "conversation_participants")
public class ParticipantEntity {

    @Id
    private UUID id;

    @Column(name = "conversation_id", insertable = false, updatable = false)
    private UUID conversationId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ParticipantRole role;

    @Column(name = "joined_at", nullable = false)
    private LocalDateTime joinedAt;

    @Column(nullable = false)
    private boolean muted;

    @Column(name = "deleted_for_user", nullable = false)
    private boolean deletedForUser;

    protected ParticipantEntity() {
    }

    public ParticipantEntity(
            UUID id,
            UUID userId,
            ParticipantRole role,
            LocalDateTime joinedAt,
            boolean muted,
            boolean deletedForUser
    ) {
        this.id = id;
        this.userId = userId;
        this.role = role;
        this.joinedAt = joinedAt;
        this.muted = muted;
        this.deletedForUser = deletedForUser;
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

    public ParticipantRole getRole() {
        return role;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    public boolean isMuted() {
        return muted;
    }

    public boolean isDeletedForUser() {
        return deletedForUser;
    }
}
