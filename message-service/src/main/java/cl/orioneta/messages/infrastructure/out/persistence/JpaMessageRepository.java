package cl.orioneta.messages.infrastructure.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * Repositorio Spring Data de mensajes.
 */
public interface JpaMessageRepository extends JpaRepository<MessageEntity, UUID> {

    List<MessageEntity> findByConversationIdAndDeletedAtIsNullOrderByCreatedAtAsc(UUID conversationId);
}
