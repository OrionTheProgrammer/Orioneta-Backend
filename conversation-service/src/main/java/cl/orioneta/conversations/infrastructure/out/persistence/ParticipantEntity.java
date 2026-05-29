package cl.orioneta.conversations.infrastructure.out.persistence;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

public class ParticipantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id", nullable = false)
    private ConversationEntity conversation;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "role", nullable = false)
    private String role;

    @Column(name = "joined_by")
    private UUID joinedBy;

    @Column(name = "last_read_at")
    private Instant lastReadAt;

    @Column(name = "muted", nullable = false)
    private Boolean muted;

    @Column(name = "active", nullable = false)
    private Boolean active;

    public ParticipantEntity() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ConversationEntity getConversation() {
        return conversation;
    }

    public void setConversation(ConversationEntity conversation) {
        this.conversation = conversation;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
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
