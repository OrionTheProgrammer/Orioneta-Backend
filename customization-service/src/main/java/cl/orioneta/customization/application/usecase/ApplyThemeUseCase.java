package cl.orioneta.customization.application.usecase;

import cl.orioneta.customization.application.dto.UserCustomizationDTO;
import cl.orioneta.customization.domain.model.UserCustomization;
import cl.orioneta.customization.domain.repository.CustomizationRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ApplyThemeUseCase {

    private final CustomizationRepositoryPort customizationRepositoryPort;

    public ApplyThemeUseCase(CustomizationRepositoryPort customizationRepositoryPort) {
        this.customizationRepositoryPort = customizationRepositoryPort;
    }

    public UserCustomization execute(UUID userId, UserCustomizationDTO request) {
        UserCustomization customization = customizationRepositoryPort.findUserCustomization(userId)
                .orElseGet(() -> UserCustomization.createDefault(userId));

        customization.update(
                request.activeGlobalThemeId(),
                request.activeFontId(),
                request.animationLevel(),
                request.compactMode()
        );

        return customizationRepositoryPort.saveUserCustomization(customization);
    }
}
