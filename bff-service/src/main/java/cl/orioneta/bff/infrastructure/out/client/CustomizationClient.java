package cl.orioneta.bff.infrastructure.out.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;
import java.util.UUID;

@FeignClient(name = "customization-service", url = "${orioneta.services.customizations}")
public interface CustomizationClient {

    @GetMapping("/api/customizations/users/{userId}")
    Map<String, Object> findUserCustomization(@PathVariable UUID userId);

    @GetMapping("/api/customizations/conversations/{conversationId}/users/{userId}")
    Map<String, Object> findConversationCustomization(@PathVariable UUID conversationId, @PathVariable UUID userId);
}
