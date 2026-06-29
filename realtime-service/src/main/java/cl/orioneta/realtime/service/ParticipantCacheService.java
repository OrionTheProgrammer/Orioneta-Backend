package cl.orioneta.realtime.service;

import cl.orioneta.realtime.client.ConversationParticipantClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Service
public class ParticipantCacheService {

    private static final Logger log = LoggerFactory.getLogger(ParticipantCacheService.class);
    private static final String CACHE_PREFIX = "participants:";
    private static final Duration CACHE_TTL = Duration.ofMinutes(5);

    private final StringRedisTemplate redisTemplate;
    private final ConversationParticipantClient participantClient;

    public ParticipantCacheService(StringRedisTemplate redisTemplate, ConversationParticipantClient participantClient) {
        this.redisTemplate = redisTemplate;
        this.participantClient = participantClient;
    }

    public List<UUID> getParticipantIds(UUID conversationId) {
        String key = CACHE_PREFIX + conversationId;
        List<UUID> cached = readCached(key);
        if (cached != null) return cached;

        List<UUID> fresh = participantClient.findParticipantIds(conversationId);
        if (!fresh.isEmpty()) {
            redisTemplate.opsForList().rightPushAll(key, fresh.stream().map(UUID::toString).toList());
            redisTemplate.expire(key, CACHE_TTL);
        }
        return fresh;
    }

    public void invalidate(UUID conversationId) {
        redisTemplate.delete(CACHE_PREFIX + conversationId);
    }

    private List<UUID> readCached(String key) {
        List<String> items = redisTemplate.opsForList().range(key, 0, -1);
        if (items == null || items.isEmpty()) return null;
        return items.stream()
                .map(s -> {
                    try { return UUID.fromString(s); } catch (Exception e) { return null; }
                })
                .filter(java.util.Objects::nonNull)
                .toList();
    }
}
