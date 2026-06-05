package cl.orioneta.messages.domain.repository;

import cl.orioneta.messages.domain.model.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Puerto de persistencia de mensajes.
 */
public interface MessageRepositoryPort {

    Message save(Message message);

    Optional<Message> findById(UUID id);

    List<Message> findByConversationId(UUID conversationId);
}
