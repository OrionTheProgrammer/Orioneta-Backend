package cl.orioneta.friendships.infrastructure.client;

import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Cliente HTTP declarativo hacia user-service.
 */
@FeignClient(name = "user-service", url = "${orioneta.services.users}")
public interface UserClient {

    @GetMapping("/api/users/{userID}")
    UserClientResponse findById(@PathVariable UUID userID);

    @GetMapping("/api/users/lookup")
    UserClientResponse findByEmail(@RequestParam String email);

    @GetMapping("/api/users/friend-code/{friendCode}")
    UserClientResponse findByFriendCode(@PathVariable String friendCode);
}
