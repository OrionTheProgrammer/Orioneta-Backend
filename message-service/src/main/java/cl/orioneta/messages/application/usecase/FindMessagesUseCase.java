package cl.orioneta.messages.application.usecase;

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

    public FindMessagesUseCase(MessageRepositoryPort messageRepositoryPort) {
        this.messageRepositoryPort = messageRepositoryPort;
    }

    public List<Message> execute(UUID conversationId) {
        return messageRepositoryPort.findByConversationId(conversationId);
    }
}
