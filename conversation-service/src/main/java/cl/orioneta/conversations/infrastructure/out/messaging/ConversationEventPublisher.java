package cl.orioneta.conversations.infrastructure.out.messaging;

import cl.orioneta.conversations.domain.event.ConversationCreatedEvent;
import cl.orioneta.conversations.domain.event.ParticipantAddedEvent;
import cl.orioneta.conversations.domain.service.ConversationEventPublisherPort;
import cl.orioneta.conversations.infrastructure.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class ConversationEventPublisher implements ConversationEventPublisherPort {

    // Consumer de mensajeria
    private final RabbitTemplate rabbitTemplate;

    public ConversationEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    // Publica eventos cuando se crea una conversacion
    @Override
    public void publishConversationCreated(ConversationCreatedEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.CONVERSATION_EXCHANGE,
                RabbitMQConfig.CONVERSATION_CREATED_KEY,
                event
        );
    }

    // Publica evento cuando se agrega un participante
    @Override
    public void publishParticipantAdded(ParticipantAddedEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.CONVERSATION_EXCHANGE,
                RabbitMQConfig.PARTICIPANT_ADDED_KEY,
                event
        );
    }
}
