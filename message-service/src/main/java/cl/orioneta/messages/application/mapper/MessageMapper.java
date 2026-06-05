package cl.orioneta.messages.application.mapper;

import cl.orioneta.messages.application.dto.MessageResponseDTO;
import cl.orioneta.messages.domain.model.Message;
import org.springframework.stereotype.Component;

/**
 * Convierte mensajes de dominio a DTOs de API.
 */
@Component
public class MessageMapper {

    public MessageResponseDTO toResponse(Message message) {
        return new MessageResponseDTO(
                message.getId(),
                message.getConversationId(),
                message.getSenderId(),
                message.getContent(),
                message.getType(),
                message.getStatus(),
                message.getCreatedAt(),
                message.getUpdatedAt(),
                message.getDeletedAt()
        );
    }
}
