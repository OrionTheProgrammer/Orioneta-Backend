package cl.orioneta.conversations.application.usecase;

import cl.orioneta.conversations.domain.exception.ConversationNotFoundException;
import cl.orioneta.conversations.domain.model.Conversation;
import cl.orioneta.conversations.domain.model.ParticipantRole;
import cl.orioneta.conversations.domain.repository.ConversationRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Agrega un usuario como miembro de un grupo.
 */
@Service
public class AddParticipantUseCase {

    private final ConversationRepositoryPort conversationRepositoryPort;

    public AddParticipantUseCase(ConversationRepositoryPort conversationRepositoryPort) {
        this.conversationRepositoryPort = conversationRepositoryPort;
    }

    public Conversation execute(UUID conversationId, UUID userId) {
        Conversation conversation = conversationRepositoryPort.findById(conversationId)
                .orElseThrow(() -> new ConversationNotFoundException("Conversacion no encontrada"));

        conversation.addParticipant(userId, ParticipantRole.MEMBER);

        return conversationRepositoryPort.save(conversation);
    }
}
