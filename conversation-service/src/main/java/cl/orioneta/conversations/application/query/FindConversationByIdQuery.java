package cl.orioneta.conversations.application.query;

import java.util.UUID;

public class FindConversationByIdQuery {

    private final UUID conversationId; // ID de la conversacion a buscar
    private final UUID requestingUserId; // ID del usuario que solicita - para validar que pertenece a la conversacion

    public FindConversationByIdQuery(UUID conversationId, UUID requestingUserId) {
        this.conversationId = conversationId;
        this.requestingUserId = requestingUserId;
    }

    public UUID getConversationId() {
        return conversationId;
    }

    public UUID getRequestingUserId() {
        return requestingUserId;
    }
}
