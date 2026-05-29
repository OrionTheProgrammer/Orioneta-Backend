package cl.orioneta.conversations.application.dto;

import cl.orioneta.conversations.domain.model.ConversationType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class ConversationResponseDTO {

    private UUID id;
    private String title;
    private ConversationType type;
    private String avatarUrl;
    private UUID createdBy;
    private List<ParticipantDTO> participants;
    private Instant createdAt;
    private Instant updatedAt;

    public ConversationResponseDTO() {
    }

    public ConversationResponseDTO(UUID id, String title, ConversationType type, String avatarUrl, UUID createdBy, List<ParticipantDTO> participants, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.avatarUrl = avatarUrl;
        this.createdBy = createdBy;
        this.participants = participants;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ConversationType getType() {
        return type;
    }

    public void setType(ConversationType type) {
        this.type = type;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public UUID getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UUID createdBy) {
        this.createdBy = createdBy;
    }

    public List<ParticipantDTO> getParticipants() {
        return participants;
    }

    public void setParticipants(List<ParticipantDTO> participants) {
        this.participants = participants;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
