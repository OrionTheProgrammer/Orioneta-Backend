package cl.orioneta.realtime.messaging;

import cl.orioneta.realtime.config.RabbitMQConfig;
import cl.orioneta.realtime.dto.RealtimeMessageDTO;
import cl.orioneta.realtime.service.RealtimeEventDispatcher;
import cl.orioneta.shared.events.MessageSentEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MessageEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(MessageEventConsumer.class);

    private final RealtimeEventDispatcher realtimeEventDispatcher;

    public MessageEventConsumer(RealtimeEventDispatcher realtimeEventDispatcher) {
        this.realtimeEventDispatcher = realtimeEventDispatcher;
    }

    @RabbitListener(queues = RabbitMQConfig.REALTIME_MESSAGE_QUEUE)
    public void consumeMessageSent(MessageSentEvent event) {
        realtimeEventDispatcher.dispatchSystemEvent(new RealtimeMessageDTO(
                "MESSAGE_SENT",
                null,
                event.conversationId(),
                event.senderId(),
                null,
                event.messageId(),
                event.messageType(),
                event.content(),
                null,
                null,
                event.occurredAt()
        ));
    }
}
