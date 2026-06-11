package cl.orioneta.messages.infrastructure.out.messaging;

import cl.orioneta.messages.application.event.MessageEventPublisher;
import cl.orioneta.messages.domain.model.Message;
import cl.orioneta.messages.infrastructure.config.RabbitMQConfig;
import cl.orioneta.shared.events.MessageSentEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@Component
public class RabbitMessageEventPublisher implements MessageEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public RabbitMessageEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publishMessageSent(Message message, List<UUID> participantIds) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.MESSAGE_EXCHANGE,
                RabbitMQConfig.MESSAGE_SENT_ROUTING_KEY,
                new MessageSentEvent(
                        message.getId(),
                        message.getConversationId(),
                        message.getSenderId(),
                        message.getContent(),
                        message.getType().name(),
                        participantIds,
                        message.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant()
                )
        );
    }
}
