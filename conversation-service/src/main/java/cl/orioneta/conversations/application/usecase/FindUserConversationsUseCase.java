package cl.orioneta.conversations.application.usecase;

import cl.orioneta.conversations.application.query.FindUserConversationsQuery;
import cl.orioneta.conversations.domain.model.Conversation;
import cl.orioneta.conversations.domain.repository.ConversationRepositoryPort;

import java.util.List;

public class FindUserConversationsUseCase {

    private final ConversationRepositoryPort conversationRepository;

    public FindUserConversationsUseCase(ConversationRepositoryPort conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    public List<Conversation> execute(FindUserConversationsQuery query) {
        return conversationRepository.findByUserId(query.getUserId());
    }
}
