package cl.orioneta.realtime.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String MESSAGE_EXCHANGE = "orioneta.messages";
    public static final String MESSAGE_SENT_ROUTING_KEY = "message.sent";
    public static final String REALTIME_MESSAGE_QUEUE = "orioneta.realtime.messages";

    @Bean
    public TopicExchange realtimeMessageExchange() {
        return new TopicExchange(MESSAGE_EXCHANGE);
    }

    @Bean
    public Queue realtimeMessageQueue() {
        return new Queue(REALTIME_MESSAGE_QUEUE, true);
    }

    @Bean
    public Binding realtimeMessageBinding(Queue realtimeMessageQueue, TopicExchange realtimeMessageExchange) {
        return BindingBuilder.bind(realtimeMessageQueue)
                .to(realtimeMessageExchange)
                .with(MESSAGE_SENT_ROUTING_KEY);
    }

    @Bean
    public MessageConverter realtimeJsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
