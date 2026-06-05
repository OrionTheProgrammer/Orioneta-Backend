package cl.orioneta.conversations.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Participante de una conversacion privada o grupal.
 */
public class Participant {

    private final UUID id;
    private final UUID conversationId;
    private final UUID userId;
    private ParticipantRole role;
    private final LocalDateTime joinedAt;
    private boolean muted;
    private boolean deletedForUser;

    private Participant(
            UUID id,
            UUID conversationId,
            UUID userId,
            ParticipantRole role,
            LocalDateTime joinedAt,
            boolean muted,
            boolean deletedForUser
    ) {
        this.id = Objects.requireNonNull(id, "El id del participante es obligatorio");
        this.conversationId = conversationId;
        this.userId = Objects.requireNonNull(userId, "El usuario participante es obligatorio");
        this.role = role == null ? ParticipantRole.MEMBER : role;
        this.joinedAt = joinedAt == null ? LocalDateTime.now() : joinedAt;
        this.muted = muted;
        this.deletedForUser = deletedForUser;
    }

    public static Participant create(UUID conversationId, UUID userId, ParticipantRole role) {
        return new Participant(UUID.randomUUID(), conversationId, userId, role, LocalDateTime.now(), false, false);
    }

    public static Participant rehydrate(
            UUID id,
            UUID conversationId,
            UUID userId,
            ParticipantRole role,
            LocalDateTime joinedAt,
            boolean muted,
            boolean deletedForUser
    ) {
        return new Participant(id, conversationId, userId, role, joinedAt, muted, deletedForUser);
    }

    public void assignConversation(UUID conversationId) {
        if (this.conversationId != null && !this.conversationId.equals(conversationId)) {
            throw new IllegalStateException("El participante ya pertenece a otra conversacion");
        }
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
