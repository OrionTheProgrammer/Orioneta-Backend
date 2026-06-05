package cl.orioneta.conversations.application.usecase;

import cl.orioneta.conversations.application.dto.CreateConversationRequestDTO;
import cl.orioneta.conversations.domain.model.Conversation;
import cl.orioneta.conversations.domain.repository.ConversationRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Crea un grupo con owner y participantes iniciales.
 */
@Service
public class CreateGroupConversationUseCase {

    private final ConversationRepositoryPort conversationRepositoryPort;

    public CreateGroupConversationUseCase(ConversationRepositoryPort conversationRepositoryPort) {
        this.conversationRepositoryPort = conversationRepositoryPort;
    }

    public Conversation execute(CreateConversationRequestDTO request) {
        UUID ownerId = request.ownerId();

        if (ownerId == null) {
            throw new IllegalArgumentException("El owner del grupo es obligatorio");
        }

        Conversation conversation = Conversation.createGroup(ownerId, request.name(), request.description(), request.participantIds());
        return conversationRepositoryPort.save(conversation);
    }
}
