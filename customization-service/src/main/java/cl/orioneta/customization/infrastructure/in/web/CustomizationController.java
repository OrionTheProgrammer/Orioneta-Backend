package cl.orioneta.customization.infrastructure.in.web;

import cl.orioneta.customization.application.dto.ConversationCustomizationDTO;
import cl.orioneta.customization.application.dto.UserCustomizationDTO;
import cl.orioneta.customization.application.mapper.CustomizationMapper;
import cl.orioneta.customization.application.usecase.ApplyThemeUseCase;
import cl.orioneta.customization.application.usecase.FindCustomizationUseCase;
import cl.orioneta.customization.application.usecase.UpdateConversationCustomizationUseCase;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/customizations")
public class CustomizationController {

    private final FindCustomizationUseCase findCustomizationUseCase;
    private final ApplyThemeUseCase applyThemeUseCase;
    private final UpdateConversationCustomizationUseCase updateConversationCustomizationUseCase;
    private final CustomizationMapper customizationMapper;

    public CustomizationController(
            FindCustomizationUseCase findCustomizationUseCase,
            ApplyThemeUseCase applyThemeUseCase,
            UpdateConversationCustomizationUseCase updateConversationCustomizationUseCase,
            CustomizationMapper customizationMapper
    ) {
        this.findCustomizationUseCase = findCustomizationUseCase;
        this.applyThemeUseCase = applyThemeUseCase;
        this.updateConversationCustomizationUseCase = updateConversationCustomizationUseCase;
        this.customizationMapper = customizationMapper;
    }

    @GetMapping("/users/{userId}")
    public UserCustomizationDTO findUserCustomization(@PathVariable UUID userId) {
        return customizationMapper.toUserDTO(findCustomizationUseCase.findUserCustomization(userId));
    }

    @PutMapping("/users/{userId}")
    public UserCustomizationDTO updateUserCustomization(@PathVariable UUID userId, @RequestBody UserCustomizationDTO request) {
        return customizationMapper.toUserDTO(applyThemeUseCase.execute(userId, request));
    }

    @GetMapping("/conversations/{conversationId}/users/{userId}")
    public ConversationCustomizationDTO findConversationCustomization(@PathVariable UUID conversationId, @PathVariable UUID userId) {
        return customizationMapper.toConversationDTO(findCustomizationUseCase.findConversationCustomization(conversationId, userId));
    }

    @PutMapping("/conversations/{conversationId}/users/{userId}")
    public ConversationCustomizationDTO updateConversationCustomization(
            @PathVariable UUID conversationId,
            @PathVariable UUID userId,
            @RequestBody ConversationCustomizationDTO request
    ) {
        return customizationMapper.toConversationDTO(updateConversationCustomizationUseCase.execute(conversationId, userId, request));
    }
}
