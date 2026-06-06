package cl.orioneta.messages.infrastructure.out.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "conversation-service", url = "${orioneta.services.conversations}")
public interface ConversationClient {

    @GetMapping("/api/conversations/{id}")
    ConversationClientResponse findById(@PathVariable UUID id);

    record ConversationClientResponse(
            UUID id,
            List<ParticipantClientResponse> participants
    ) {
    }

    record ParticipantClientResponse(UUID userId) {
    }
}
