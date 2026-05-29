package cl.orioneta.conversations.domain.model;

import java.time.Instant;
import java.util.UUID;

public class Participant {

    private UUID id;
    private UUID conversationId; // Conversacion a la que pertenece el participante
    private UUID userId; // id del participante
    private ParticipantRole role; // ADMIN o MIEMBRO | ADMIN or MEMBER
    private UUID joinedBy; // UNIDO por alguien
    private Instant lastReadAt; // Ultima vez que usuario leyo el mensaje escrito
    private Boolean muted; // si el usuario silecio conversacion = Mutear
    private Boolean active; // si el participante sigue Activo - false cuando abandona o lo sacan del grupo

    public Participant() {
    }

    public Participant(UUID id, UUID conversationId, UUID userId, ParticipantRole role, UUID joinedBy, Instant lastReadAt, Boolean muted, Boolean active) {
        this.id = id;
        this.conversationId = conversationId;
        this.userId = userId;
        this.role = role;
        this.joinedBy = joinedBy;
        this.lastReadAt = lastReadAt;
        this.muted = muted != null ? muted : false;
        this.active = active != null ? active : true;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getConversationId() {
        return conversationId;
    }

    public void setConversationId(UUID conversationId) {
        this.conversationId = conversationId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public ParticipantRole getRole() {
        return role;
    }

    public void setRole(ParticipantRole role) {
        this.role = role;
    }

    public UUID getJoinedBy() {
        return joinedBy;
    }

    public void setJoinedBy(UUID joinedBy) {
        this.joinedBy = joinedBy;
    }

    public Instant getLastReadAt() {
        return lastReadAt;
    }

    public void setLastReadAt(Instant lastReadAt) {
        this.lastReadAt = lastReadAt;
    }

    public Boolean getMuted() {
        return muted;
    }

    public void setMuted(Boolean muted) {
        this.muted = muted;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
