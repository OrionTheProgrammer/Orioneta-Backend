package cl.orioneta.messages.application.event;

import cl.orioneta.messages.domain.model.Message;

import java.util.List;
import java.util.UUID;

public interface MessageEventPublisher {

    void publishMessageSent(Message message, List<UUID> participantIds);
}
