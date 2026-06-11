package cl.orioneta.friendships.infrastructure.client;

import cl.orioneta.friendships.app.client.ConversationDirectory;
import cl.orioneta.friendships.domain.exception.ConversationCreationException;
import feign.FeignException;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;

/**
 * Adaptador que conecta friendship-service con conversation-service.
 */
@Component
public class ConversationClientAdapter implements ConversationDirectory {

    private static final String PRIVATE_CHAT = "PRIVATE_CHAT";

    private final ConversationClient conversationClient;

    public ConversationClientAdapter(ConversationClient conversationClient) {
        this.conversationClient = conversationClient;
    }

    @Override
    public UUID createPrivateConversation(UUID firstUserId, UUID secondUserId) {
        try {
            ConversationClient.ConversationClientResponse response = conversationClient.createConversation(
                    new ConversationClient.CreateConversationClientRequest(
                            PRIVATE_CHAT,
                            null,
                            null,
                            firstUserId,
                            List.of(firstUserId, secondUserId)
                    )
            );

            return response.id();
        } catch (FeignException exception) {
            throw new ConversationCreationException("No se pudo crear el chat privado para la amistad");
        }
    }
}
