package cl.orioneta.conversations.application.dto;

import java.util.List;
import java.util.UUID;

public class CreateConversationRequestDTO {

    private UUID recipientId;
    private String title;
    private List<UUID> memberIds;

    public CreateConversationRequestDTO() {
    }

    public CreateConversationRequestDTO(UUID recipientId, String title, List<UUID> memberIds) {
        this.recipientId = recipientId;
        this.title = title;
        this.memberIds = memberIds;
    }

    public UUID getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(UUID recipientId) {
        this.recipientId = recipientId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<UUID> getMemberIds() {
        return memberIds;
    }

    public void setMemberIds(List<UUID> memberIds) {
        this.memberIds = memberIds;
    }
}
