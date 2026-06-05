package cl.orioneta.conversations.application.usecase;

import cl.orioneta.conversations.application.dto.CreateConversationRequestDTO;
import cl.orioneta.conversations.domain.model.Conversation;
import cl.orioneta.conversations.domain.repository.ConversationRepositoryPort;
import org.springframework.stereotype.Service;

/**
 * Crea una conversacion privada entre dos usuarios.
 */
@Service
public class CreatePrivateConversationUseCase {

    private final ConversationRepositoryPort conversationRepositoryPort;

    public CreatePrivateConversationUseCase(ConversationRepositoryPort conversationRepositoryPort) {
        this.conversationRepositoryPort = conversationRepositoryPort;
    }

    public Conversation execute(CreateConversationRequestDTO request) {
        if (request.participantIds().size() != 2) {
            throw new IllegalArgumentException("Un chat privado necesita dos participantes");
        }

        Conversation conversation = Conversation.createPrivate(
                request.participantIds().get(0),
                request.participantIds().get(1)
        );

        return conversationRepositoryPort.save(conversation);
    }
}
