package cl.orioneta.messages.infrastructure.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String MESSAGE_EXCHANGE = "orioneta.messages";
    public static final String MESSAGE_SENT_ROUTING_KEY = "message.sent";

    @Bean
    public TopicExchange messageExchange() {
        return new TopicExchange(MESSAGE_EXCHANGE);
    }

    @Bean
    public MessageConverter messageJsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
