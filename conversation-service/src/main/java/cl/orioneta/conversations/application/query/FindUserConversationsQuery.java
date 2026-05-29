package cl.orioneta.conversations.application.query;

import java.util.UUID;

public class FindUserConversationsQuery {

    private final UUID userId; // ID del usuario del que se quieren obtener las conversaciones

    public FindUserConversationsQuery(UUID userId) {
        this.userId = userId;
    }

    public UUID getUserId() {
        return userId;
    }
}
