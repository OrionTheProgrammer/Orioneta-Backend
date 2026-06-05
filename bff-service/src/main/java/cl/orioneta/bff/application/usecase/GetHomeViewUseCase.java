package cl.orioneta.bff.application.usecase;

import cl.orioneta.bff.application.dto.HomeViewDTO;
import cl.orioneta.bff.infrastructure.out.client.ConversationClient;
import cl.orioneta.bff.infrastructure.out.client.CustomizationClient;
import cl.orioneta.bff.infrastructure.out.client.NotificationClient;
import cl.orioneta.bff.infrastructure.out.client.UserClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * Arma la pantalla inicial del usuario consultando servicios internos.
 */
@Service
public class GetHomeViewUseCase {

    private final UserClient userClient;
    private final ConversationClient conversationClient;
    private final NotificationClient notificationClient;
    private final CustomizationClient customizationClient;

    public GetHomeViewUseCase(
            UserClient userClient,
            ConversationClient conversationClient,
            NotificationClient notificationClient,
            CustomizationClient customizationClient
    ) {
        this.userClient = userClient;
        this.conversationClient = conversationClient;
        this.notificationClient = notificationClient;
        this.customizationClient = customizationClient;
    }

    public HomeViewDTO execute(UUID userId) {
        return new HomeViewDTO(
                userId,
                safe(() -> userClient.findById(userId), Map.of()),
                safe(() -> conversationClient.findByUserId(userId), List.of()),
                safe(() -> notificationClient.findByUserId(userId), List.of()),
                safe(() -> customizationClient.findUserCustomization(userId), Map.of())
        );
    }

    private <T> T safe(Supplier<T> action, T fallback) {
        try {
            return action.get();
        } catch (RuntimeException exception) {
            return fallback;
        }
    }
}
