package cl.orioneta.notifications.infrastructure.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.UUID;

@Service
public class UserServiceClient {

    private static final Logger log = LoggerFactory.getLogger(UserServiceClient.class);

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public UserServiceClient(
            RestTemplateBuilder builder,
            @Value("${orioneta.services.user:http://localhost:8083}") String baseUrl
    ) {
        this.restTemplate = builder.build();
        this.baseUrl = baseUrl;
    }

    public UserProfile getUserProfile(UUID userId) {
        if (userId == null) return null;
        try {
            var response = restTemplate.getForEntity(
                    baseUrl + "/api/users/{id}",
                    Map.class,
                    userId
            );
            Map<String, Object> body = response.getBody();
            if (body == null) return null;

            return new UserProfile(
                    (String) body.getOrDefault("displayName", null),
                    (String) body.getOrDefault("profilePhoto", null)
            );
        } catch (Exception e) {
            log.warn("Failed to fetch user profile for {}: {}", userId, e.getMessage());
            return null;
        }
    }

    public record UserProfile(String displayName, String profilePhoto) {}
}
