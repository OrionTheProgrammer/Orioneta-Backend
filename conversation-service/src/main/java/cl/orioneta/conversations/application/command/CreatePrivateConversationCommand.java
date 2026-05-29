package cl.orioneta.conversations.application.command;

import java.util.UUID;

public class CreatePrivateConversationCommand {

    private final UUID creatorId; // ID de quien crea la conversacion
    private final UUID recipientId; // ID con quien se crea la conversacion
    private final String title; // titulo de la conversación

    public CreatePrivateConversationCommand(UUID creatorId, UUID recipientId, String title) {
        this.creatorId = creatorId;
        this.recipientId = recipientId;
        this.title = title;
    }

    public UUID getCreatorId() {
        return creatorId;
    }

    public UUID getRecipientId() {
        return recipientId;
    }

    public String getTitle() {
        return title;
    }
}
