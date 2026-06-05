package cl.orioneta.bff.application.usecase;

import cl.orioneta.bff.application.dto.ChatViewDTO;
import cl.orioneta.bff.infrastructure.out.client.ConversationClient;
import cl.orioneta.bff.infrastructure.out.client.CustomizationClient;
import cl.orioneta.bff.infrastructure.out.client.MessageClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * Arma la vista de un chat con conversacion, mensajes y personalizacion.
 */
@Service
public class GetChatViewUseCase {

    private final ConversationClient conversationClient;
    private final MessageClient messageClient;
    private final CustomizationClient customizationClient;

    public GetChatViewUseCase(
            ConversationClient conversationClient,
            MessageClient messageClient,
            CustomizationClient customizationClient
    ) {
        this.conversationClient = conversationClient;
        this.messageClient = messageClient;
        this.customizationClient = customizationClient;
    }

    public ChatViewDTO execute(UUID conversationId, UUID userId) {
        return new ChatViewDTO(
                conversationId,
                userId,
                safe(() -> conversationClient.findById(conversationId), Map.of()),
                safe(() -> messageClient.findByConversationId(conversationId), List.of()),
                safe(() -> customizationClient.findConversationCustomization(conversationId, userId), Map.of())
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
