package cl.orioneta.messages.application.usecase;

import cl.orioneta.messages.application.dto.SendMessageRequestDTO;
import cl.orioneta.messages.application.conversation.ConversationLookupPort;
import cl.orioneta.messages.application.conversation.ConversationParticipantSummary;
import cl.orioneta.messages.application.conversation.ConversationSummary;
import cl.orioneta.messages.application.event.MessageEventPublisher;
import cl.orioneta.messages.domain.model.Message;
import cl.orioneta.messages.domain.repository.MessageRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Crea y guarda un mensaje nuevo.
 */
@Service
public class SendMessageUseCase {

    private final MessageRepositoryPort messageRepositoryPort;
    private final ConversationLookupPort conversationLookupPort;
    private final MessageEventPublisher messageEventPublisher;

    public SendMessageUseCase(
            MessageRepositoryPort messageRepositoryPort,
            ConversationLookupPort conversationLookupPort,
            MessageEventPublisher messageEventPublisher
    ) {
        this.messageRepositoryPort = messageRepositoryPort;
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

        Message savedMessage = messageRepositoryPort.save(message);
        messageEventPublisher.publishMessageSent(savedMessage, participantIds(conversation));

        return savedMessage;
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
