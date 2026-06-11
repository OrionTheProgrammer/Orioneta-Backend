package cl.orioneta.notifications.infrastructure.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String MESSAGE_EXCHANGE = "orioneta.messages";
    public static final String MESSAGE_SENT_ROUTING_KEY = "message.sent";
    public static final String FRIENDSHIP_EXCHANGE = "orioneta.friendships";
    public static final String FRIEND_REQUEST_SENT_ROUTING_KEY = "friend.request.sent";
    public static final String FRIEND_REQUEST_ACCEPTED_ROUTING_KEY = "friend.request.accepted";

    public static final String NOTIFICATION_MESSAGE_QUEUE = "orioneta.notifications.messages";
    public static final String NOTIFICATION_FRIEND_REQUEST_SENT_QUEUE = "orioneta.notifications.friend-requests.sent";
    public static final String NOTIFICATION_FRIEND_REQUEST_ACCEPTED_QUEUE = "orioneta.notifications.friend-requests.accepted";

    @Bean
    public TopicExchange notificationMessageExchange() {
        return new TopicExchange(MESSAGE_EXCHANGE);
    }

    @Bean
    public TopicExchange notificationFriendshipExchange() {
        return new TopicExchange(FRIENDSHIP_EXCHANGE);
    }

    @Bean
    public Queue notificationMessageQueue() {
        return new Queue(NOTIFICATION_MESSAGE_QUEUE, true);
    }

    @Bean
    public Queue notificationFriendRequestSentQueue() {
        return new Queue(NOTIFICATION_FRIEND_REQUEST_SENT_QUEUE, true);
    }

    @Bean
    public Queue notificationFriendRequestAcceptedQueue() {
        return new Queue(NOTIFICATION_FRIEND_REQUEST_ACCEPTED_QUEUE, true);
    }

    @Bean
    public Binding notificationMessageBinding(
            @Qualifier("notificationMessageQueue") Queue notificationMessageQueue,
            @Qualifier("notificationMessageExchange") TopicExchange notificationMessageExchange
    ) {
        return BindingBuilder.bind(notificationMessageQueue)
                .to(notificationMessageExchange)
                .with(MESSAGE_SENT_ROUTING_KEY);
    }

    @Bean
    public Binding notificationFriendRequestSentBinding(
            @Qualifier("notificationFriendRequestSentQueue") Queue notificationFriendRequestSentQueue,
            @Qualifier("notificationFriendshipExchange") TopicExchange notificationFriendshipExchange
    ) {
        return BindingBuilder.bind(notificationFriendRequestSentQueue)
                .to(notificationFriendshipExchange)
                .with(FRIEND_REQUEST_SENT_ROUTING_KEY);
    }

    @Bean
    public Binding notificationFriendRequestAcceptedBinding(
            @Qualifier("notificationFriendRequestAcceptedQueue") Queue notificationFriendRequestAcceptedQueue,
            @Qualifier("notificationFriendshipExchange") TopicExchange notificationFriendshipExchange
    ) {
        return BindingBuilder.bind(notificationFriendRequestAcceptedQueue)
                .to(notificationFriendshipExchange)
                .with(FRIEND_REQUEST_ACCEPTED_ROUTING_KEY);
    }

    @Bean
    public MessageConverter notificationJsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
