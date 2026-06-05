package cl.orioneta.messages.application.usecase;

import cl.orioneta.messages.application.dto.EditMessageRequestDTO;
import cl.orioneta.messages.domain.exception.MessageNotFoundException;
import cl.orioneta.messages.domain.model.Message;
import cl.orioneta.messages.domain.repository.MessageRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Edita el contenido de un mensaje existente.
 */
@Service
public class EditMessageUseCase {

    private final MessageRepositoryPort messageRepositoryPort;

    public EditMessageUseCase(MessageRepositoryPort messageRepositoryPort) {
        this.messageRepositoryPort = messageRepositoryPort;
    }

    public Message execute(UUID id, EditMessageRequestDTO request) {
        Message message = messageRepositoryPort.findById(id)
                .orElseThrow(() -> new MessageNotFoundException("Mensaje no encontrado"));

        message.edit(request.content());

        return messageRepositoryPort.save(message);
    }
}
