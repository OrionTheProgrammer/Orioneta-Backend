package cl.orioneta.friendships.infrastructure.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuracion minima de RabbitMQ para eventos de amistad.
 */
@Configuration
public class RabbitMQConfig {

    public static final String FRIENDSHIP_EXCHANGE = "orioneta.friendships";
    public static final String FRIEND_REQUEST_SENT_ROUTING_KEY = "friend.request.sent";
    public static final String FRIEND_REQUEST_ACCEPTED_ROUTING_KEY = "friend.request.accepted";

    @Bean
    public TopicExchange friendshipExchange() {
        return new TopicExchange(FRIENDSHIP_EXCHANGE);
    }

    @Bean
    public MessageConverter friendshipJsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
