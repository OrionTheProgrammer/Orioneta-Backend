package cl.orioneta.customization.infrastructure.out.persistence;

import cl.orioneta.customization.domain.model.ConversationCustomization;
import cl.orioneta.customization.domain.model.UserCustomization;
import cl.orioneta.customization.domain.repository.CustomizationRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class CustomizationPersistenceAdapter implements CustomizationRepositoryPort {

    private final JpaCustomizationRepository userRepository;
    private final JpaConversationCustomizationRepository conversationRepository;

    public CustomizationPersistenceAdapter(
            JpaCustomizationRepository userRepository,
            JpaConversationCustomizationRepository conversationRepository
    ) {
        this.userRepository = userRepository;
        this.conversationRepository = conversationRepository;
    }

    @Override
    public UserCustomization saveUserCustomization(UserCustomization customization) {
        return toDomain(userRepository.save(toEntity(customization)));
    }

    @Override
    public Optional<UserCustomization> findUserCustomization(UUID userId) {
        return userRepository.findByUserId(userId).map(this::toDomain);
    }

    @Override
    public ConversationCustomization saveConversationCustomization(ConversationCustomization customization) {
        return toDomain(conversationRepository.save(toEntity(customization)));
    }

    @Override
    public Optional<ConversationCustomization> findConversationCustomization(UUID conversationId, UUID userId) {
        return conversationRepository.findByConversationIdAndUserId(conversationId, userId).map(this::toDomain);
    }

    private UserCustomizationEntity toEntity(UserCustomization customization) {
        return new UserCustomizationEntity(
                customization.getId(),
                customization.getUserId(),
                customization.getActiveGlobalThemeId(),
                customization.getActiveFontId(),
                customization.getAnimationLevel(),
                customization.isCompactMode(),
                customization.getCreatedAt(),
                customization.getUpdatedAt()
        );
    }

    private UserCustomization toDomain(UserCustomizationEntity entity) {
        return UserCustomization.rehydrate(
                entity.getId(),
                entity.getUserId(),
                entity.getActiveGlobalThemeId(),
                entity.getActiveFontId(),
                entity.getAnimationLevel(),
                entity.isCompactMode(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    private ConversationCustomizationEntity toEntity(ConversationCustomization customization) {
        return new ConversationCustomizationEntity(
                customization.getId(),
                customization.getConversationId(),
                customization.getUserId(),
                customization.getActiveChatThemeId(),
                customization.getActiveBackgroundId(),
                customization.getBubbleStyle(),
                customization.getFontSize(),
                customization.getCreatedAt(),
                customization.getUpdatedAt()
        );
    }

    private ConversationCustomization toDomain(ConversationCustomizationEntity entity) {
        return ConversationCustomization.rehydrate(
                entity.getId(),
                entity.getConversationId(),
                entity.getUserId(),
                entity.getActiveChatThemeId(),
                entity.getActiveBackgroundId(),
                entity.getBubbleStyle(),
                entity.getFontSize(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
