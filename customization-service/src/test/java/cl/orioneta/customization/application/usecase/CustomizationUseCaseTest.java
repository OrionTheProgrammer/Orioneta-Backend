package cl.orioneta.customization.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cl.orioneta.customization.application.dto.ConversationCustomizationDTO;
import cl.orioneta.customization.application.dto.UserCustomizationDTO;
import cl.orioneta.customization.domain.model.BubbleStyle;
import cl.orioneta.customization.domain.model.ConversationCustomization;
import cl.orioneta.customization.domain.model.UserCustomization;
import cl.orioneta.customization.domain.repository.CustomizationRepositoryPort;
import java.util.Optional;
import java.util.UUID;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Pruebas de personalizacion global y por conversacion.
 */
@ExtendWith(MockitoExtension.class)
class CustomizationUseCaseTest {

    private final Faker faker = new Faker();

    @Mock
    private CustomizationRepositoryPort customizationRepositoryPort;

    private FindCustomizationUseCase findUseCase;
    private ApplyThemeUseCase applyThemeUseCase;
    private UpdateConversationCustomizationUseCase updateConversationUseCase;

    @BeforeEach
    void setUp() {
        findUseCase = new FindCustomizationUseCase(customizationRepositoryPort);
        applyThemeUseCase = new ApplyThemeUseCase(customizationRepositoryPort);
        updateConversationUseCase = new UpdateConversationCustomizationUseCase(customizationRepositoryPort);
    }

    @Test
    void findUserCustomizationCreatesDefaultWhenMissing() {
        UUID userId = UUID.randomUUID();
        when(customizationRepositoryPort.findUserCustomization(userId)).thenReturn(Optional.empty());
        when(customizationRepositoryPort.saveUserCustomization(any(UserCustomization.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UserCustomization customization = findUseCase.findUserCustomization(userId);

        verify(customizationRepositoryPort).saveUserCustomization(any(UserCustomization.class));
        assertThat(customization.getUserId()).isEqualTo(userId);
        assertThat(customization.getActiveGlobalThemeId()).isEqualTo("default");
        assertThat(customization.getActiveFontId()).isEqualTo("system");
    }

    @Test
    void applyThemeUpdatesExistingCustomizationAndClampsAnimationLevel() {
        UUID userId = UUID.randomUUID();
        UserCustomization existing = UserCustomization.createDefault(userId);
        UserCustomizationDTO request = new UserCustomizationDTO(
                existing.getId(),
                userId,
                "  " + faker.color().name() + "  ",
                "Inter",
                99,
                true,
                existing.getCreatedAt(),
                existing.getUpdatedAt()
        );
        when(customizationRepositoryPort.findUserCustomization(userId)).thenReturn(Optional.of(existing));
        when(customizationRepositoryPort.saveUserCustomization(any(UserCustomization.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UserCustomization customization = applyThemeUseCase.execute(userId, request);

        assertThat(customization.getActiveGlobalThemeId()).isEqualTo(request.activeGlobalThemeId().trim());
        assertThat(customization.getActiveFontId()).isEqualTo("Inter");
        assertThat(customization.getAnimationLevel()).isEqualTo(5);
        assertThat(customization.isCompactMode()).isTrue();
    }

    @Test
    void updateConversationCustomizationCreatesDefaultAndAppliesChanges() {
        UUID conversationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        ConversationCustomizationDTO request = new ConversationCustomizationDTO(
                null,
                conversationId,
                userId,
                "aurora",
                "background-1",
                BubbleStyle.ROUNDED,
                18,
                null,
                null
        );
        when(customizationRepositoryPort.findConversationCustomization(conversationId, userId)).thenReturn(Optional.empty());
        when(customizationRepositoryPort.saveConversationCustomization(any(ConversationCustomization.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ConversationCustomization customization = updateConversationUseCase.execute(conversationId, userId, request);

        assertThat(customization.getConversationId()).isEqualTo(conversationId);
        assertThat(customization.getUserId()).isEqualTo(userId);
        assertThat(customization.getActiveChatThemeId()).isEqualTo("aurora");
        assertThat(customization.getActiveBackgroundId()).isEqualTo("background-1");
        assertThat(customization.getBubbleStyle()).isEqualTo(BubbleStyle.ROUNDED);
        assertThat(customization.getFontSize()).isEqualTo(18);
    }
}
