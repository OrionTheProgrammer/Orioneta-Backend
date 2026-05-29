package cl.orioneta.conversations.domain.event;

import java.time.Instant;
import java.util.UUID;

public class ParticipantAddedEvent {

    private final UUID conversationId; // ID de la conversacion donde se añadido el participante
    private final UUID userId;  // ID del usuario que ha sido añadido
    private final UUID addedBy; // ID del admin que lo añadio
    private final Instant occurredAt; // Momento exacto en que ocurrio el evento

    public ParticipantAddedEvent(UUID conversationId, UUID userId, UUID addedBy) {
        this.conversationId = conversationId;
        this.userId = userId;
        this.addedBy = addedBy;
        this.occurredAt = Instant.now();
    }

    public UUID getConversationId() {
        return conversationId;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getAddedBy() {
        return addedBy;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }
}

