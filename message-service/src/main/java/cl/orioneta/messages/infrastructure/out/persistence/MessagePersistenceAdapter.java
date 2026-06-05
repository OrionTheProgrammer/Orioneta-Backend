package cl.orioneta.messages.infrastructure.out.persistence;

import cl.orioneta.messages.domain.model.Message;
import cl.orioneta.messages.domain.repository.MessageRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Adaptador JPA del puerto de mensajes.
 */
@Repository
public class MessagePersistenceAdapter implements MessageRepositoryPort {

    private final JpaMessageRepository jpaMessageRepository;

    public MessagePersistenceAdapter(JpaMessageRepository jpaMessageRepository) {
        this.jpaMessageRepository = jpaMessageRepository;
    }

    @Override
    public Message save(Message message) {
        return toDomain(jpaMessageRepository.save(toEntity(message)));
    }

    @Override
    public Optional<Message> findById(UUID id) {
        return jpaMessageRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public List<Message> findByConversationId(UUID conversationId) {
        return jpaMessageRepository.findByConversationIdAndDeletedAtIsNullOrderByCreatedAtAsc(conversationId)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    private MessageEntity toEntity(Message message) {
        return new MessageEntity(
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

    private Message toDomain(MessageEntity entity) {
        return Message.rehydrate(
                entity.getId(),
                entity.getConversationId(),
                entity.getSenderId(),
                entity.getContent(),
                entity.getType(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt()
        );
    }
}
