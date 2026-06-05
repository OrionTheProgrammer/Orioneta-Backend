package cl.orioneta.messages.application.usecase;

import cl.orioneta.messages.application.dto.SendMessageRequestDTO;
import cl.orioneta.messages.domain.model.Message;
import cl.orioneta.messages.domain.repository.MessageRepositoryPort;
import org.springframework.stereotype.Service;

/**
 * Crea y guarda un mensaje nuevo.
 */
@Service
public class SendMessageUseCase {

    private final MessageRepositoryPort messageRepositoryPort;

    public SendMessageUseCase(MessageRepositoryPort messageRepositoryPort) {
        this.messageRepositoryPort = messageRepositoryPort;
    }

    public Message execute(SendMessageRequestDTO request) {
        Message message = Message.create(
                request.conversationId(),
                request.senderId(),
                request.content(),
                request.type()
        );

        return messageRepositoryPort.save(message);
    }
}
