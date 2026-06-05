package cl.orioneta.bff.infrastructure.out.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@FeignClient(name = "conversation-service", url = "${orioneta.services.conversations}")
public interface ConversationClient {

    @GetMapping("/api/conversations/{id}")
    Map<String, Object> findById(@PathVariable UUID id);

    @GetMapping("/api/conversations/users/{userId}")
    List<Map<String, Object>> findByUserId(@PathVariable UUID userId);
}
