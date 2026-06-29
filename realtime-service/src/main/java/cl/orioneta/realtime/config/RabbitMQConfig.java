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
    public static final String FRIENDSHIP_EXCHANGE = "orioneta.friendships";
    public static final String CONVERSATION_EXCHANGE = "orioneta.conversations";

    public static final String MESSAGE_SENT_ROUTING_KEY = "message.sent";
    public static final String MESSAGE_READ_ROUTING_KEY = "message.read";
    public static final String FRIEND_REQUEST_SENT_ROUTING_KEY = "friend.request.sent";
    public static final String FRIEND_REQUEST_ACCEPTED_ROUTING_KEY = "friend.request.accepted";
    public static final String CONVERSATION_CREATED_ROUTING_KEY = "conversation.created";
    public static final String GROUP_INVITATION_ROUTING_KEY = "group.invitation";
    public static final String USER_ONLINE_ROUTING_KEY = "user.online";

    public static final String REALTIME_MESSAGE_QUEUE = "orioneta.realtime.messages";
    public static final String REALTIME_MESSAGE_READ_QUEUE = "orioneta.realtime.messages.read";
    public static final String REALTIME_NOTIFICATION_QUEUE = "orioneta.realtime.notifications";
    public static final String REALTIME_FRIEND_REQUEST_SENT_QUEUE = "orioneta.realtime.friend-requests.sent";
    public static final String REALTIME_FRIEND_REQUEST_ACCEPTED_QUEUE = "orioneta.realtime.friend-requests.accepted";
    public static final String REALTIME_CONVERSATION_CREATED_QUEUE = "orioneta.realtime.conversations.created";
    public static final String REALTIME_GROUP_INVITATION_QUEUE = "orioneta.realtime.group-invitations";

    @Bean
    public TopicExchange realtimeMessageExchange() {
        return new TopicExchange(MESSAGE_EXCHANGE);
    }

    @Bean
    public TopicExchange realtimeFriendshipExchange() {
        return new TopicExchange(FRIENDSHIP_EXCHANGE);
    }

    @Bean
    public TopicExchange realtimeConversationExchange() {
        return new TopicExchange(CONVERSATION_EXCHANGE);
    }

    @Bean
    public Queue realtimeMessageQueue() {
        return new Queue(REALTIME_MESSAGE_QUEUE, true);
    }

    @Bean
    public Queue realtimeMessageReadQueue() {
        return new Queue(REALTIME_MESSAGE_READ_QUEUE, true);
    }

    @Bean
    public Queue realtimeNotificationQueue() {
        return new Queue(REALTIME_NOTIFICATION_QUEUE, true);
    }

    @Bean
    public Queue realtimeFriendRequestSentQueue() {
        return new Queue(REALTIME_FRIEND_REQUEST_SENT_QUEUE, true);
    }

    @Bean
    public Queue realtimeFriendRequestAcceptedQueue() {
        return new Queue(REALTIME_FRIEND_REQUEST_ACCEPTED_QUEUE, true);
    }

    @Bean
    public Queue realtimeConversationCreatedQueue() {
        return new Queue(REALTIME_CONVERSATION_CREATED_QUEUE, true);
    }

    @Bean
    public Queue realtimeGroupInvitationQueue() {
        return new Queue(REALTIME_GROUP_INVITATION_QUEUE, true);
    }

    @Bean
    public Binding realtimeMessageBinding() {
        return BindingBuilder.bind(realtimeMessageQueue())
                .to(realtimeMessageExchange())
                .with(MESSAGE_SENT_ROUTING_KEY);
    }

    @Bean
    public Binding realtimeMessageReadBinding() {
        return BindingBuilder.bind(realtimeMessageReadQueue())
                .to(realtimeMessageExchange())
                .with(MESSAGE_READ_ROUTING_KEY);
    }

    @Bean
    public Binding realtimeFriendRequestSentBinding() {
        return BindingBuilder.bind(realtimeFriendRequestSentQueue())
                .to(realtimeFriendshipExchange())
                .with(FRIEND_REQUEST_SENT_ROUTING_KEY);
    }

    @Bean
    public Binding realtimeFriendRequestAcceptedBinding() {
        return BindingBuilder.bind(realtimeFriendRequestAcceptedQueue())
                .to(realtimeFriendshipExchange())
                .with(FRIEND_REQUEST_ACCEPTED_ROUTING_KEY);
    }

    @Bean
    public Binding realtimeConversationCreatedBinding() {
        return BindingBuilder.bind(realtimeConversationCreatedQueue())
                .to(realtimeConversationExchange())
                .with(CONVERSATION_CREATED_ROUTING_KEY);
    }

    @Bean
    public Binding realtimeGroupInvitationBinding() {
        return BindingBuilder.bind(realtimeGroupInvitationQueue())
                .to(realtimeConversationExchange())
                .with(GROUP_INVITATION_ROUTING_KEY);
    }

    @Bean
    public MessageConverter realtimeJsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
