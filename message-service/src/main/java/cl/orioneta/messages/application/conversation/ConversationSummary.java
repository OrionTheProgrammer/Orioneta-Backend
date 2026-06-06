package cl.orioneta.messages.application.conversation;

import java.util.List;
import java.util.UUID;

/**
 * Vista minima de una conversacion requerida por los casos de uso de mensajes.
 */
public record ConversationSummary(
        UUID id,
        List<ConversationParticipantSummary> participants
) {

    public boolean hasParticipant(UUID userId) {
        return participants != null && participants.stream()
                .anyMatch(participant -> userId.equals(participant.userId()));
    }
}
