package cl.orioneta.bff.infrastructure.out.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@FeignClient(name = "friendship-service", url = "${orioneta.services.friendships}")
public interface FriendshipClient {

    @GetMapping("/api/friendships/users/{userId}")
    List<Map<String, Object>> findFriends(@PathVariable UUID userId);
}
