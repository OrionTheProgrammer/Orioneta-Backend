package cl.orioneta.customization.application.mapper;

import cl.orioneta.customization.application.dto.ConversationCustomizationDTO;
import cl.orioneta.customization.application.dto.UserCustomizationDTO;
import cl.orioneta.customization.domain.model.ConversationCustomization;
import cl.orioneta.customization.domain.model.UserCustomization;
import org.springframework.stereotype.Component;

@Component
public class CustomizationMapper {

    public UserCustomizationDTO toUserDTO(UserCustomization customization) {
        return new UserCustomizationDTO(
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

    public ConversationCustomizationDTO toConversationDTO(ConversationCustomization customization) {
        return new ConversationCustomizationDTO(
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
}
