package cl.orioneta.messages.infrastructure.out.client;

import cl.orioneta.messages.application.conversation.ConversationLookupPort;
import cl.orioneta.messages.application.conversation.ConversationParticipantSummary;
import cl.orioneta.messages.application.conversation.ConversationSummary;
import feign.FeignException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class ConversationLookupAdapter implements ConversationLookupPort {

    private final ConversationClient conversationClient;

    public ConversationLookupAdapter(ConversationClient conversationClient) {
        this.conversationClient = conversationClient;
    }

    @Override
    public ConversationSummary findById(UUID conversationId) {
        try {
            ConversationClient.ConversationClientResponse response = conversationClient.findById(conversationId);
            return new ConversationSummary(response.id(), mapParticipants(response.participants()));
        } catch (FeignException.NotFound exception) {
            throw new IllegalArgumentException("La conversacion no existe");
        } catch (FeignException exception) {
            throw new IllegalStateException("No se pudo validar la conversacion en conversation-service");
        }
    }

    private List<ConversationParticipantSummary> mapParticipants(
            List<ConversationClient.ParticipantClientResponse> participants
    ) {
        if (participants == null) {
            return List.of();
        }

        return participants.stream()
                .map(participant -> new ConversationParticipantSummary(participant.userId()))
                .toList();
    }
}
