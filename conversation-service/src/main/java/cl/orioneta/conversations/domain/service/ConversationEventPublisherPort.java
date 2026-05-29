package cl.orioneta.conversations.domain.service;

import cl.orioneta.conversations.domain.event.ConversationCreatedEvent;
import cl.orioneta.conversations.domain.event.ParticipantAddedEvent;

public interface ConversationEventPublisherPort {

    void publishConversationCreated(ConversationCreatedEvent event);

    void publishParticipantAdded(ParticipantAddedEvent event);
}
