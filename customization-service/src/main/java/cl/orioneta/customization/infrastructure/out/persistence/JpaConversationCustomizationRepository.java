package cl.orioneta.customization.infrastructure.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaConversationCustomizationRepository extends JpaRepository<ConversationCustomizationEntity, UUID> {

    Optional<ConversationCustomizationEntity> findByConversationIdAndUserId(UUID conversationId, UUID userId);
}
