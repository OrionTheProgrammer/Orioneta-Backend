package cl.orioneta.customization.application.usecase;

import cl.orioneta.customization.domain.model.ConversationCustomization;
import cl.orioneta.customization.domain.model.UserCustomization;
import cl.orioneta.customization.domain.repository.CustomizationRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FindCustomizationUseCase {

    private final CustomizationRepositoryPort customizationRepositoryPort;

    public FindCustomizationUseCase(CustomizationRepositoryPort customizationRepositoryPort) {
        this.customizationRepositoryPort = customizationRepositoryPort;
    }

    public UserCustomization findUserCustomization(UUID userId) {
        return customizationRepositoryPort.findUserCustomization(userId)
                .orElseGet(() -> customizationRepositoryPort.saveUserCustomization(UserCustomization.createDefault(userId)));
    }

    public ConversationCustomization findConversationCustomization(UUID conversationId, UUID userId) {
        return customizationRepositoryPort.findConversationCustomization(conversationId, userId)
                .orElseGet(() -> customizationRepositoryPort.saveConversationCustomization(ConversationCustomization.createDefault(conversationId, userId)));
    }
}
