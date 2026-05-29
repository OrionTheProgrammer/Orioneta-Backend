package cl.orioneta.conversations.infrastructure.out.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import cl.orioneta.conversations.domain.model.Conversation;
import cl.orioneta.conversations.domain.model.ConversationType;
import cl.orioneta.conversations.domain.model.Participant;
import cl.orioneta.conversations.domain.model.ParticipantRole;
import cl.orioneta.conversations.domain.repository.ConversationRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ConversationPersistenceAdapter implements ConversationRepositoryPort {

    private final JpaConversationRepository jpaRepository;

    public ConversationPersistenceAdapter(JpaConversationRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Conversation save(Conversation conversation) {
        ConversationEntity entity = toEntity(conversation);

        entityManager.createNativeQuery(
                        "INSERT INTO conversations (id, title, type, avatar_url, created_by, created_at, updated_at) " +
                                "VALUES (:id, :title, :type, :avatarUrl, :createdBy, :createdAt, :updatedAt)")
                .setParameter("id", entity.getId())
                .setParameter("title", entity.getTitle())
                .setParameter("type", entity.getType())
                .setParameter("avatarUrl", entity.getAvatarUrl())
                .setParameter("createdBy", entity.getCreatedBy())
                .setParameter("createdAt", entity.getCreatedAt())
                .setParameter("updatedAt", entity.getUpdatedAt())
                .executeUpdate();

        for (ParticipantEntity p : entity.getParticipants()) {
            entityManager.createNativeQuery(
                            "INSERT INTO participants (id, conversation_id, user_id, role, joined_by, last_read_at, muted, active) " +
                                    "VALUES (:id, :conversationId, :userId, :role, :joinedBy, :lastReadAt, :muted, :active)")
                    .setParameter("id", p.getId())
                    .setParameter("conversationId", entity.getId())
                    .setParameter("userId", p.getUserId())
                    .setParameter("role", p.getRole())
                    .setParameter("joinedBy", p.getJoinedBy())
                    .setParameter("lastReadAt", p.getLastReadAt())
                    .setParameter("muted", p.getMuted())
                    .setParameter("active", p.getActive())
                    .executeUpdate();
        }

        return toDomain(entity);
    }

    @Override
    public Optional<Conversation> findById(UUID id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Conversation> findByUserId(UUID userId) {
        return jpaRepository.findByUserId(userId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsDirectConversation(UUID userIdA, UUID userIdB) {
        return jpaRepository.existsDirectConversation(userIdA, userIdB);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }

    // Dominio a Entidad
    private ConversationEntity toEntity(Conversation conversation) {
        ConversationEntity entity = new ConversationEntity();
        entity.setId(conversation.getId());
        entity.setTitle(conversation.getTitle());
        entity.setType(conversation.getType().name());
        entity.setAvatarUrl(conversation.getAvatarUrl());
        entity.setCreatedBy(conversation.getCreatedBy());
        entity.setCreatedAt(conversation.getCreatedAt());
        entity.setUpdatedAt(conversation.getUpdatedAt());

        List<ParticipantEntity> participantEntities = conversation.getParticipants().stream()
                .map(p -> toParticipantEntity(p, entity))
                .collect(Collectors.toList());
        entity.setParticipants(participantEntities);

        return entity;
    }

    // Entidad a Dominio
    private Conversation toDomain(ConversationEntity entity) {
        List<Participant> participants = entity.getParticipants().stream()
                .map(this::toParticipantDomain)
                .collect(Collectors.toList());

        return Conversation.rehydrate(
                entity.getId(),
                ConversationType.valueOf(entity.getType()),
                entity.getCreatedBy(),
                entity.getTitle(),
                entity.getAvatarUrl(),
                participants,
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    // Participant dominio a ParticipantEntity
    private ParticipantEntity toParticipantEntity(Participant participant, ConversationEntity conversationEntity) {
        ParticipantEntity entity = new ParticipantEntity();
        entity.setId(participant.getId());
        entity.setConversation(conversationEntity);
        entity.setUserId(participant.getUserId());
        entity.setRole(participant.getRole().name());
        entity.setJoinedBy(participant.getJoinedBy());
        entity.setLastReadAt(participant.getLastReadAt());
        entity.setMuted(participant.getMuted());
        entity.setActive(participant.getActive());
        return entity;
    }


    // ParticipantEntity a Participant dominio
    private Participant toParticipantDomain(ParticipantEntity entity) {
        return new Participant(
                entity.getId(),
                entity.getConversation().getId(),
                entity.getUserId(),
                ParticipantRole.valueOf(entity.getRole()),
                entity.getJoinedBy(),
                entity.getLastReadAt(),
                entity.getMuted(),
                entity.getActive()
        );
    }

}

