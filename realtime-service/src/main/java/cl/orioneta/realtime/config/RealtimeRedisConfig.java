package cl.orioneta.realtime.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RealtimeRedisConfig {

    public static final String PRESENCE_PREFIX = "presence:";
    public static final String USER_SESSION_PREFIX = "user-sessions:";
    public static final String PRESENCE_CHANNEL = "orioneta:presence";

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisConnectionFactory connectionFactory,
            MessageListenerAdapter presenceListenerAdapter
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(presenceListenerAdapter, new PatternTopic(PRESENCE_CHANNEL));
        return container;
    }

    @Bean
    public MessageListenerAdapter presenceListenerAdapter(RedisPresenceSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "onMessage");
    }
}
