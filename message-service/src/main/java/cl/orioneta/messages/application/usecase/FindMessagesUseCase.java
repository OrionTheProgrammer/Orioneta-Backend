package cl.orioneta.messages.application.usecase;

import cl.orioneta.messages.application.conversation.ConversationLookupPort;
import cl.orioneta.messages.domain.model.Message;
import cl.orioneta.messages.domain.repository.MessageRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Lista mensajes visibles de una conversacion.
 */
@Service
public class FindMessagesUseCase {

    private final MessageRepositoryPort messageRepositoryPort;
    private final ConversationLookupPort conversationLookupPort;

    public FindMessagesUseCase(MessageRepositoryPort messageRepositoryPort, ConversationLookupPort conversationLookupPort) {
        this.messageRepositoryPort = messageRepositoryPort;
        this.conversationLookupPort = conversationLookupPort;
    }

    public List<Message> execute(UUID conversationId) {
        conversationLookupPort.findById(conversationId);
        return messageRepositoryPort.findByConversationId(conversationId);
    }
}
