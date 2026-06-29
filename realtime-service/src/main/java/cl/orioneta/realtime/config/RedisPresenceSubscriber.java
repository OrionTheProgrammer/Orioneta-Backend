package cl.orioneta.realtime.config;

import cl.orioneta.realtime.service.RedisPresenceService;
import cl.orioneta.realtime.websocket.WebSocketSessionRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RedisPresenceSubscriber {

    private static final Logger log = LoggerFactory.getLogger(RedisPresenceSubscriber.class);

    private final WebSocketSessionRegistry sessionRegistry;
    private final ObjectMapper objectMapper;
    private final RedisPresenceService redisPresenceService;

    public RedisPresenceSubscriber(
            WebSocketSessionRegistry sessionRegistry,
            ObjectMapper objectMapper,
            RedisPresenceService redisPresenceService
    ) {
        this.sessionRegistry = sessionRegistry;
        this.objectMapper = objectMapper;
        this.redisPresenceService = redisPresenceService;
    }

    @SuppressWarnings("unused")
    public void onMessage(String message) {
        try {
            Map<String, Object> event = objectMapper.readValue(message, Map.class);
            String instanceId = (String) event.get("instanceId");

            if (redisPresenceService.getInstanceId().equals(instanceId)) {
                return;
            }

            sessionRegistry.broadcast(message);
        } catch (Exception e) {
            log.warn("Error processing presence event from Redis pub/sub", e);
        }
    }
}
