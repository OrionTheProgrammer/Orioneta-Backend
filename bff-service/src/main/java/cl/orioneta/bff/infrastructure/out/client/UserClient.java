package cl.orioneta.bff.infrastructure.out.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;
import java.util.UUID;

@FeignClient(name = "user-service", url = "${orioneta.services.users}")
public interface UserClient {

    @GetMapping("/api/users/{id}")
    Map<String, Object> findById(@PathVariable UUID id);
}
