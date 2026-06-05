package cl.orioneta.conversations.infrastructure.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

/**
 * Repositorio Spring Data de conversaciones.
 */
public interface JpaConversationRepository extends JpaRepository<ConversationEntity, UUID> {

    @Query("""
            select distinct conversation
            from ConversationEntity conversation
            join conversation.participants participant
            where participant.userId = :userId
              and participant.deletedForUser = false
              and conversation.deletedAt is null
            order by conversation.updatedAt desc
            """)
    List<ConversationEntity> findVisibleByParticipantUserId(@Param("userId") UUID userId);
}
