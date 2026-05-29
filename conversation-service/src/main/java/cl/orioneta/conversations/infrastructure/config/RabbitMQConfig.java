package cl.orioneta.conversations.infrastructure.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Exchanges
    public static final String CONVERSATION_EXCHANGE = "conversation.exchange";

    // Routing keys
    public static final String CONVERSATION_CREATED_KEY = "conversation.created";
    public static final String PARTICIPANT_ADDED_KEY = "conversation.participant.added";

    //Queues
    public static final String CONVERSATION_CREATED_QUEUE = "conversation.created.queue";
    public static final String PARTICIPANT_ADDED_QUEUE = "conversation.participant.added.queue";

    @Bean
    public TopicExchange conversationExchange() {
        return new TopicExchange(CONVERSATION_EXCHANGE);
    }

    @Bean
    public Queue conversationCreatedQueue() {
        return QueueBuilder.durable(CONVERSATION_CREATED_QUEUE).build();
    }

    @Bean
    public Queue participantAddedQueue() {
        return QueueBuilder.durable(PARTICIPANT_ADDED_QUEUE).build();
    }

    @Bean
    public Binding conversationCreatedBinding() {
        return BindingBuilder
                .bind(conversationCreatedQueue())
                .to(conversationExchange())
                .with(CONVERSATION_CREATED_KEY);
    }

    @Bean
    public Binding participantAddedBinding() {
        return BindingBuilder
                .bind(participantAddedQueue())
                .to(conversationExchange())
                .with(PARTICIPANT_ADDED_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}
