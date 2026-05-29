package cl.orioneta.conversations.infrastructure.out.persistence;

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

    @Override
    public Conversation save(Conversation conversation) {
        ConversationEntity entity = toEntity(conversation);
        ConversationEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
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

