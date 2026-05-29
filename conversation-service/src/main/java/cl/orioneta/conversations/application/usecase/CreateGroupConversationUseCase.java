package cl.orioneta.conversations.application.usecase;

import cl.orioneta.conversations.application.command.CreateGroupConversationCommand;
import cl.orioneta.conversations.domain.model.Conversation;
import cl.orioneta.conversations.domain.model.ConversationType;
import cl.orioneta.conversations.domain.model.Participant;
import cl.orioneta.conversations.domain.model.ParticipantRole;
import cl.orioneta.conversations.domain.repository.ConversationRepositoryPort;

import java.util.UUID;

public class CreateGroupConversationUseCase {

    private final ConversationRepositoryPort conversationRepository;

    public CreateGroupConversationUseCase(ConversationRepositoryPort conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    public Conversation execute(CreateGroupConversationCommand command) {

        // Crear el grupo
        Conversation conversation = Conversation.create(
                command.getTitle(), ConversationType.GROUP, command.getCreatorId()
        );

        // Agregar al creador como ADMIN
        conversation.addParticipant(new Participant(
                UUID.randomUUID(), conversation.getId(),
                command.getCreatorId(), ParticipantRole.ADMIN,
                command.getCreatorId(), null, false, true
        ));

        // agregar los miembros iniciales
        conversation.addParticipant(new Participant(
                UUID.randomUUID(), conversation.getId(),
                command.getCreatorId(), ParticipantRole.MEMBER,
                command.getCreatorId(), null, false, true
        ));

        // Persistir y retornar
        return conversationRepository.save(conversation);

    }
}
