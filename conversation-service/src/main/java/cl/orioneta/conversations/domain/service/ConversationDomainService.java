package cl.orioneta.conversations.domain.service;

import cl.orioneta.conversations.domain.model.Conversation;
import cl.orioneta.conversations.domain.model.ConversationType;
import cl.orioneta.conversations.domain.model.ParticipantRole;
import cl.orioneta.conversations.domain.repository.ConversationRepositoryPort;

import java.util.Objects;
import java.util.UUID;

public class ConversationDomainService {

    private final ConversationRepositoryPort conversationRepository;

    public ConversationDomainService(ConversationRepositoryPort conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    // NO puede existir una conversacion DIRECTA duplicada entre 2 usuarios
    public void validateDirectConversationNotExists(UUID userIdA, UUID userIdB) {
        if (conversationRepository.existsDirectConversation(userIdA, userIdB)) {
            throw new IllegalStateException("Ya existe una conversación directa entre estos usuarios");
        }
    }

    // Solo el ADMIN puede agregar a participantes a un grupo
    public void validateCanAddParticipant(Conversation conversation, UUID requestingUserId) {
        Objects.requireNonNull(requestingUserId, "El userId es obligatorio");
        boolean isAdmin = conversation.getParticipants().stream()
                .anyMatch(p -> p.getUserId().equals(requestingUserId)
                        && p.getRole() == ParticipantRole.ADMIN);
        if (!isAdmin) {
            throw new IllegalStateException("Solo un ADMIN puede agregar participantes");
        }
    }

    // una conversacion DIRECTA solo puede tener 2 participantes
    public void validateParticipantLimit(Conversation conversation) {
        if (conversation.getType() == ConversationType.DIRECT
                && conversation.getParticipants().size() >= 2) {
            throw new IllegalStateException("Una conversación directa solo puede tener 2 participantes");
        }
    }

}
