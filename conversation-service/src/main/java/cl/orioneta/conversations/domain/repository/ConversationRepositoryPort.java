package cl.orioneta.conversations.domain.repository;

import cl.orioneta.conversations.domain.model.Conversation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Puerto que el dominio/app usa para persistir conversaciones.
 */
public interface ConversationRepositoryPort {

    Conversation save(Conversation conversation);

    Optional<Conversation> findById(UUID id);

    List<Conversation> findByParticipantUserId(UUID userId);

    boolean existsById(UUID id);

    void deleteById(UUID id);
}
