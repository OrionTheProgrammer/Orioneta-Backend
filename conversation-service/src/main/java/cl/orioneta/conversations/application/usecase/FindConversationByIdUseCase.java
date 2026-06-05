package cl.orioneta.conversations.application.usecase;

import cl.orioneta.conversations.domain.exception.ConversationNotFoundException;
import cl.orioneta.conversations.domain.model.Conversation;
import cl.orioneta.conversations.domain.repository.ConversationRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Busca una conversacion por identificador.
 */
@Service
public class FindConversationByIdUseCase {

    private final ConversationRepositoryPort conversationRepositoryPort;

    public FindConversationByIdUseCase(ConversationRepositoryPort conversationRepositoryPort) {
        this.conversationRepositoryPort = conversationRepositoryPort;
    }

    public Conversation execute(UUID id) {
        return conversationRepositoryPort.findById(id)
                .orElseThrow(() -> new ConversationNotFoundException("Conversacion no encontrada"));
    }
}
