package cl.orioneta.customization.domain.repository;

import cl.orioneta.customization.domain.model.ConversationCustomization;
import cl.orioneta.customization.domain.model.UserCustomization;

import java.util.Optional;
import java.util.UUID;

public interface CustomizationRepositoryPort {

    UserCustomization saveUserCustomization(UserCustomization customization);

    Optional<UserCustomization> findUserCustomization(UUID userId);

    ConversationCustomization saveConversationCustomization(ConversationCustomization customization);

    Optional<ConversationCustomization> findConversationCustomization(UUID conversationId, UUID userId);
}
