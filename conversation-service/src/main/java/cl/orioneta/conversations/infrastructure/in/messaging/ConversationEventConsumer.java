package cl.orioneta.conversations.infrastructure.in.messaging;

import cl.orioneta.conversations.domain.event.ConversationCreatedEvent;
import cl.orioneta.conversations.domain.event.ParticipantAddedEvent;
import cl.orioneta.conversations.infrastructure.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ConversationEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(ConversationEventConsumer.class);

    // Escucha cuando se crea una conversacion
    @RabbitListener(queues = RabbitMQConfig.CONVERSATION_CREATED_QUEUE)
    public void onConversationCreated(ConversationCreatedEvent event) {
        log.info("Conversación creada: id={}, type={}, createdBy={}",
                event.getConversationId(), event.getType(), event.getCreatedBy());
                // creacion de los otros microservicios
                // reaccion de microservicio: notificacion, realtime
    }

    // Escucha cuando se agrega un participante
    @RabbitListener(queues = RabbitMQConfig.PARTICIPANT_ADDED_QUEUE)
    public void onParticipantAdded(ParticipantAddedEvent event) {
        log.info("Participante agregado: conversationId={}, userId={}, addedBy={}",
                event.getConversationId(), event.getUserId(), event.getAddedBy());
    }

}
