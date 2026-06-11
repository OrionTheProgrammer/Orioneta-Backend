package cl.orioneta.bff.infrastructure.out.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@FeignClient(name = "conversation-service", url = "${orioneta.services.conversations}")
public interface ConversationClient {

    @PostMapping("/api/conversations")
    Map<String, Object> createConversation(@RequestBody Map<String, Object> request);

    @GetMapping("/api/conversations/{id}")
    Map<String, Object> findById(@PathVariable UUID id);

    @GetMapping("/api/conversations/users/{userId}")
    List<Map<String, Object>> findByUserId(@PathVariable UUID userId);
}
