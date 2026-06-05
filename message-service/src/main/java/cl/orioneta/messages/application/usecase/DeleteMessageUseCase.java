package cl.orioneta.messages.application.usecase;

import cl.orioneta.messages.domain.exception.MessageNotFoundException;
import cl.orioneta.messages.domain.model.Message;
import cl.orioneta.messages.domain.repository.MessageRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Elimina logicamente un mensaje.
 */
@Service
public class DeleteMessageUseCase {

    private final MessageRepositoryPort messageRepositoryPort;

    public DeleteMessageUseCase(MessageRepositoryPort messageRepositoryPort) {
        this.messageRepositoryPort = messageRepositoryPort;
    }

    public void execute(UUID id) {
        Message message = messageRepositoryPort.findById(id)
                .orElseThrow(() -> new MessageNotFoundException("Mensaje no encontrado"));

        message.delete();
        messageRepositoryPort.save(message);
    }
}
