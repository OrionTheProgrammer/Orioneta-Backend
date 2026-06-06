package cl.orioneta.messages.application.usecase;

import cl.orioneta.messages.application.dto.SendMessageRequestDTO;
import cl.orioneta.messages.application.conversation.ConversationLookupPort;
import cl.orioneta.messages.application.conversation.ConversationSummary;
import cl.orioneta.messages.domain.model.Message;
import cl.orioneta.messages.domain.repository.MessageRepositoryPort;
import org.springframework.stereotype.Service;

/**
 * Crea y guarda un mensaje nuevo.
 */
@Service
public class SendMessageUseCase {

    private final MessageRepositoryPort messageRepositoryPort;
    private final ConversationLookupPort conversationLookupPort;

    public SendMessageUseCase(MessageRepositoryPort messageRepositoryPort, ConversationLookupPort conversationLookupPort) {
        this.messageRepositoryPort = messageRepositoryPort;
        this.conversationLookupPort = conversationLookupPort;
    }

    public Message execute(SendMessageRequestDTO request) {
        ConversationSummary conversation = conversationLookupPort.findById(request.conversationId());

        if (!conversation.hasParticipant(request.senderId())) {
            throw new IllegalArgumentException("El emisor no participa en la conversacion");
        }

        Message message = Message.create(
                request.conversationId(),
                request.senderId(),
                request.content(),
                request.type()
        );

        return messageRepositoryPort.save(message);
    }
}
