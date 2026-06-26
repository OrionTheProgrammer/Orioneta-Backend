package cl.orioneta.conversations.application.usecase;

import cl.orioneta.conversations.domain.exception.ConversationNotFoundException;
import cl.orioneta.conversations.domain.model.Conversation;
import cl.orioneta.conversations.domain.repository.ConversationRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RemoveParticipantUseCase {

    private final ConversationRepositoryPort repository;

    public RemoveParticipantUseCase(ConversationRepositoryPort repository) {
        this.repository = repository;
    }

    public void execute(UUID conversationId, UUID userId) {
        Conversation conversation = repository.findById(conversationId)
                .orElseThrow(() ->
                        new ConversationNotFoundException(conversationId.toString())
                );
        conversation.removeParticipant(userId);
        repository.save(conversation);
    }
}