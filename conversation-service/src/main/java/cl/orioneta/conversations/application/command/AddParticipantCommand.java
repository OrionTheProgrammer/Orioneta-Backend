package cl.orioneta.conversations.application.command;

import java.util.UUID;

public class AddParticipantCommand {

    private final UUID conversationId; // ID conversacion donde se agrega
    private final UUID requestingUserId; // ID quien solicita agregar (debe ser ADMIN)
    private final UUID newUserId; // ID usuario a agregar

    public AddParticipantCommand(UUID conversationId, UUID requestingUserId, UUID newUserId) {
        this.conversationId = conversationId;
        this.requestingUserId = requestingUserId;
        this.newUserId = newUserId;
    }

    public UUID getConversationId() {
        return conversationId;
    }

    public UUID getRequestingUserId() {
        return requestingUserId;
    }

    public UUID getNewUserId() {
        return newUserId;
    }
}
