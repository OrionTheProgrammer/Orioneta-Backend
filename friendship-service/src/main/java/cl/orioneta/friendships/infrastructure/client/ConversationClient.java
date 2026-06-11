package cl.orioneta.friendships.infrastructure.client;

import java.util.List;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Cliente HTTP declarativo hacia conversation-service.
 */
@FeignClient(name = "conversation-service", url = "${orioneta.services.conversations}")
public interface ConversationClient {

    @PostMapping("/api/conversations")
    ConversationClientResponse createConversation(@RequestBody CreateConversationClientRequest request);

    record CreateConversationClientRequest(
            String type,
            String name,
            String description,
            UUID ownerId,
            List<UUID> participantIds
    ) {
    }

    record ConversationClientResponse(UUID id) {
    }
}
