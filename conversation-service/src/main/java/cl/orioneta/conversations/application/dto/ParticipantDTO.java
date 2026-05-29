package cl.orioneta.conversations.application.dto;

import cl.orioneta.conversations.domain.model.ParticipantRole;

import java.time.Instant;
import java.util.UUID;

public class ParticipantDTO {

    private UUID id;
    private UUID userId;
    private ParticipantRole role;
    private Instant lastReadAt;
    private Boolean muted;
    private Boolean active;

    public ParticipantDTO() {
    }

    public ParticipantDTO(UUID id, UUID userId, ParticipantRole role, Instant lastReadAt, Boolean muted, Boolean active) {
        this.id = id;
        this.userId = userId;
        this.role = role;
        this.lastReadAt = lastReadAt;
        this.muted = muted;
        this.active = active;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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
