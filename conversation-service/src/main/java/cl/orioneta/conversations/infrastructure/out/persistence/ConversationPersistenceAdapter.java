package cl.orioneta.conversations.infrastructure.out.persistence;

import cl.orioneta.conversations.domain.model.Conversation;
import cl.orioneta.conversations.domain.model.Participant;
import cl.orioneta.conversations.domain.repository.ConversationRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Adaptador JPA del puerto de conversaciones.
 */
@Repository
public class ConversationPersistenceAdapter implements ConversationRepositoryPort {

    private final JpaConversationRepository jpaConversationRepository;

    public ConversationPersistenceAdapter(JpaConversationRepository jpaConversationRepository) {
        this.jpaConversationRepository = jpaConversationRepository;
    }

    @Override
    public Conversation save(Conversation conversation) {
        return toDomain(jpaConversationRepository.save(toEntity(conversation)));
    }

    @Override
    public Optional<Conversation> findById(UUID id) {
        return jpaConversationRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public List<Conversation> findByParticipantUserId(UUID userId) {
        return jpaConversationRepository.findVisibleByParticipantUserId(userId)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaConversationRepository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        jpaConversationRepository.deleteById(id);
    }


    private ConversationEntity toEntity(Conversation conversation) {
        return new ConversationEntity(
                conversation.getId(),
                conversation.getType(),
                conversation.getName(),
                conversation.getDescription(),
                conversation.getOwnerId(),
                conversation.getAvatarUrl(),
                conversation.getBackgroundUrl(),
                conversation.getCreatedAt(),
                conversation.getUpdatedAt(),
                conversation.getDeletedAt(),
                conversation.getParticipants().stream().map(this::toParticipantEntity).toList()
        );
    }

    private ParticipantEntity toParticipantEntity(Participant participant) {
        return new ParticipantEntity(
                participant.getId(),
                participant.getUserId(),
                participant.getRole(),
                participant.getJoinedAt(),
                participant.isMuted(),
                participant.isDeletedForUser()
        );
    }

    private Conversation toDomain(ConversationEntity entity) {
        return Conversation.rehydrate(
                entity.getId(),
                entity.getType(),
                entity.getName(),
                entity.getDescription(),
                entity.getOwnerId(),
                entity.getAvatarUrl(),
                entity.getBackgroundUrl(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt(),
                entity.getParticipants().stream().map(participant -> toParticipantDomain(entity.getId(), participant)).toList()
        );
    }

    private Participant toParticipantDomain(UUID conversationId, ParticipantEntity entity) {
        return Participant.rehydrate(
                entity.getId(),
                conversationId,
                entity.getUserId(),
                entity.getRole(),
                entity.getJoinedAt(),
                entity.isMuted(),
                entity.isDeletedForUser()
        );
    }
}
