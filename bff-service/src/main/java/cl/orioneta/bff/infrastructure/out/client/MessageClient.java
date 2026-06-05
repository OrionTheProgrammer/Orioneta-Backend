package cl.orioneta.bff.infrastructure.out.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@FeignClient(name = "message-service", url = "${orioneta.services.messages}")
public interface MessageClient {

    @GetMapping("/api/messages/conversation/{conversationId}")
    List<Map<String, Object>> findByConversationId(@PathVariable UUID conversationId);

    @PostMapping("/api/messages")
    Map<String, Object> sendMessage(@RequestBody Map<String, Object> request);
}
