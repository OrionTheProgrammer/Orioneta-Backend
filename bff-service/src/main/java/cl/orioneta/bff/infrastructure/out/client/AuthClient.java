package cl.orioneta.bff.infrastructure.out.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@FeignClient(name = "auth-service", url = "${orioneta.services.auth}")
public interface AuthClient {

    @GetMapping("/api/auth/oauth2/providers")
    List<Map<String, Object>> findOAuth2Providers();
}
