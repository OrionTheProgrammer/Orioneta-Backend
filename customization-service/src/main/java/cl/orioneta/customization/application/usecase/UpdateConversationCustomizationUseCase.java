package cl.orioneta.customization.application.usecase;

import cl.orioneta.customization.application.dto.ConversationCustomizationDTO;
import cl.orioneta.customization.domain.model.ConversationCustomization;
import cl.orioneta.customization.domain.repository.CustomizationRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UpdateConversationCustomizationUseCase {

    private final CustomizationRepositoryPort customizationRepositoryPort;

    public UpdateConversationCustomizationUseCase(CustomizationRepositoryPort customizationRepositoryPort) {
        this.customizationRepositoryPort = customizationRepositoryPort;
    }

    public ConversationCustomization execute(UUID conversationId, UUID userId, ConversationCustomizationDTO request) {
        ConversationCustomization customization = customizationRepositoryPort.findConversationCustomization(conversationId, userId)
                .orElseGet(() -> ConversationCustomization.createDefault(conversationId, userId));

        customization.update(
                request.activeChatThemeId(),
                request.activeBackgroundId(),
                request.bubbleStyle(),
                request.fontSize()
        );

        return customizationRepositoryPort.saveConversationCustomization(customization);
    }
}
