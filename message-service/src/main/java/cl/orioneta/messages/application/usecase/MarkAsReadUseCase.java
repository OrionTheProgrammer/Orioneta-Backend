package cl.orioneta.messages.application.usecase;

import cl.orioneta.messages.domain.exception.MessageNotFoundException;
import cl.orioneta.messages.domain.model.Message;
import cl.orioneta.messages.domain.repository.MessageRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Marca un mensaje como leido.
 */
@Service
@Transactional
public class MarkAsReadUseCase {

    private final MessageRepositoryPort messageRepositoryPort;

    public MarkAsReadUseCase(MessageRepositoryPort messageRepositoryPort) {
        this.messageRepositoryPort = messageRepositoryPort;
    }

    public Message execute(UUID id) {
        Message message = messageRepositoryPort.findById(id)
                .orElseThrow(() -> new MessageNotFoundException("Mensaje no encontrado"));

        message.markAsRead();

        return messageRepositoryPort.save(message);
    }
}
