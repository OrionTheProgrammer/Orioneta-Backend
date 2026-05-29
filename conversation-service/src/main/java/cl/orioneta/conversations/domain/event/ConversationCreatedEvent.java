package cl.orioneta.conversations.domain.event;

import cl.orioneta.conversations.domain.model.ConversationType;

import java.time.Instant;
import java.util.UUID;

public class ConversationCreatedEvent {

    private final UUID conversationId; // ID de la conversacion
    private final ConversationType type; // Tipo de conversacion DIRECTA o GRUPO
    private final UUID createdBy; // ID de quien lo creo
    private final Instant occurredAt; //

    public ConversationCreatedEvent(UUID conversationId, ConversationType type, UUID createdBy) {
        this.conversationId = conversationId;
        this.type = type;
        this.createdBy = createdBy;
        this.occurredAt = Instant.now();
    }

    public UUID getConversationId() {
        return conversationId;
    }

    public ConversationType getType() {
        return type;
    }

    public UUID getCreatedBy() {
        return createdBy;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }
}
