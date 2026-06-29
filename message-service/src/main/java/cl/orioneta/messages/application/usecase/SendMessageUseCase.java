package cl.orioneta.messages.application.usecase;

import cl.orioneta.messages.application.dto.SendMessageRequestDTO;
import cl.orioneta.messages.application.conversation.ConversationLookupPort;
import cl.orioneta.messages.application.conversation.ConversationParticipantSummary;
import cl.orioneta.messages.application.conversation.ConversationSummary;
import cl.orioneta.messages.application.event.MessageEventPublisher;
import cl.orioneta.messages.domain.model.Message;
import cl.orioneta.messages.infrastructure.out.persistence.AsyncMessagePersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Crea y guarda un mensaje nuevo.
 */
@Service
public class SendMessageUseCase {

    private final AsyncMessagePersister asyncMessagePersister;
    private final ConversationLookupPort conversationLookupPort;
    private final MessageEventPublisher messageEventPublisher;

    public SendMessageUseCase(
            AsyncMessagePersister asyncMessagePersister,
            ConversationLookupPort conversationLookupPort,
            MessageEventPublisher messageEventPublisher
    ) {
        this.asyncMessagePersister = asyncMessagePersister;
        this.conversationLookupPort = conversationLookupPort;
        this.messageEventPublisher = messageEventPublisher;
    }

    public Message execute(SendMessageRequestDTO request) {
        ConversationSummary conversation = conversationLookupPort.findById(request.conversationId());

        if (!conversation.hasParticipant(request.senderId())) {
            throw new IllegalArgumentException("El emisor no participa en la conversacion");
        }

        Message message = Message.create(
                request.conversationId(),
                request.senderId(),
                request.content(),
                request.type()
        );

        messageEventPublisher.publishMessageSent(message, participantIds(conversation));
        asyncMessagePersister.persistAsync(message);

        return message;
    }

    private List<UUID> participantIds(ConversationSummary conversation) {
        if (conversation.participants() == null) {
            return List.of();
        }

        return conversation.participants()
                .stream()
                .map(ConversationParticipantSummary::userId)
                .toList();
    }
}
