package cl.orioneta.conversations.infrastructure.out.messaging;

import cl.orioneta.conversations.domain.event.ConversationCreatedEvent;
import cl.orioneta.conversations.domain.event.ParticipantAddedEvent;
import cl.orioneta.conversations.infrastructure.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class ConversationEventPublisher {

    // Consumer de mensajeria
    private final RabbitTemplate rabbitTemplate;

    public ConversationEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    // Publica eventos cuando se crea una conversacion
    public void publishConversationCreated(ConversationCreatedEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.CONVERSATION_EXCHANGE,
                RabbitMQConfig.CONVERSATION_CREATED_KEY,
                event
        );
    }

    // Publica evento cuando se agrega un participante
    public void publishParticipantAdded(ParticipantAddedEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.CONVERSATION_EXCHANGE,
                RabbitMQConfig.PARTICIPANT_ADDED_KEY,
                event
        );
    }
}
