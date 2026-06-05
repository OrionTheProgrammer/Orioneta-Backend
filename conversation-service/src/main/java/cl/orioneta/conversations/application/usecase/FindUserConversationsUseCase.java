package cl.orioneta.conversations.application.usecase;

import cl.orioneta.conversations.domain.model.Conversation;
import cl.orioneta.conversations.domain.repository.ConversationRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Lista las conversaciones visibles para un usuario.
 */
@Service
public class FindUserConversationsUseCase {

    private final ConversationRepositoryPort conversationRepositoryPort;

    public FindUserConversationsUseCase(ConversationRepositoryPort conversationRepositoryPort) {
        this.conversationRepositoryPort = conversationRepositoryPort;
    }

    public List<Conversation> execute(UUID userId) {
        return conversationRepositoryPort.findByParticipantUserId(userId);
    }
}
