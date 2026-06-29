package cl.orioneta.realtime.service;

import cl.orioneta.realtime.config.RealtimeRedisConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class RedisPresenceService {

    private static final Logger log = LoggerFactory.getLogger(RedisPresenceService.class);
    private static final Duration PRESENCE_TTL = Duration.ofSeconds(90);

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final String instanceId;

    public RedisPresenceService(StringRedisTemplate redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.instanceId = UUID.randomUUID().toString().substring(0, 8);
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void markOnline(UUID userId, String sessionId) {
        String key = RealtimeRedisConfig.PRESENCE_PREFIX + userId;
        redisTemplate.opsForValue().set(key, sessionId, PRESENCE_TTL);

        redisTemplate.opsForSet().add(RealtimeRedisConfig.USER_SESSION_PREFIX + userId, sessionId);
        redisTemplate.expire(RealtimeRedisConfig.USER_SESSION_PREFIX + userId, PRESENCE_TTL);

        publishPresenceChange(userId, true);
    }

    public void markOffline(UUID userId, String sessionId) {
        redisTemplate.opsForSet().remove(RealtimeRedisConfig.USER_SESSION_PREFIX + userId, sessionId);

        Long size = redisTemplate.opsForSet().size(RealtimeRedisConfig.USER_SESSION_PREFIX + userId);
        if (size == null || size == 0) {
            redisTemplate.delete(RealtimeRedisConfig.PRESENCE_PREFIX + userId);
            redisTemplate.delete(RealtimeRedisConfig.USER_SESSION_PREFIX + userId);
            publishPresenceChange(userId, false);
        }
    }

    public boolean isOnline(UUID userId) {
        String key = RealtimeRedisConfig.PRESENCE_PREFIX + userId;
        Boolean hasKey = redisTemplate.hasKey(key);
        return Boolean.TRUE.equals(hasKey);
    }

    public void refreshPresence(UUID userId, String sessionId) {
        String key = RealtimeRedisConfig.PRESENCE_PREFIX + userId;
        redisTemplate.expire(key, PRESENCE_TTL);
        redisTemplate.expire(RealtimeRedisConfig.USER_SESSION_PREFIX + userId, PRESENCE_TTL);
    }

    private void publishPresenceChange(UUID userId, boolean online) {
        try {
            String message = objectMapper.writeValueAsString(Map.of(
                    "type", online ? "USER_CONNECTED" : "USER_DISCONNECTED",
                    "userId", userId.toString(),
                    "instanceId", instanceId,
                    "occurredAt", Instant.now().toString()
            ));
            redisTemplate.convertAndSend(RealtimeRedisConfig.PRESENCE_CHANNEL, message);
        } catch (JsonProcessingException e) {
            log.warn("Error serializing presence event", e);
        }
    }

    public Set<String> getUserSessions(UUID userId) {
        return redisTemplate.opsForSet().members(RealtimeRedisConfig.USER_SESSION_PREFIX + userId);
    }
}
