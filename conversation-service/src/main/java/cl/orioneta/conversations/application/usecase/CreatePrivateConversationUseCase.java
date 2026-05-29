package cl.orioneta.conversations.application.usecase;

import cl.orioneta.conversations.application.command.CreatePrivateConversationCommand;
import cl.orioneta.conversations.domain.event.ConversationCreatedEvent;
import cl.orioneta.conversations.domain.model.Conversation;
import cl.orioneta.conversations.domain.model.ConversationType;
import cl.orioneta.conversations.domain.model.Participant;
import cl.orioneta.conversations.domain.model.ParticipantRole;
import cl.orioneta.conversations.domain.service.ConversationEventPublisherPort;
import cl.orioneta.conversations.domain.repository.ConversationRepositoryPort;
import cl.orioneta.conversations.domain.service.ConversationDomainService;

import java.util.UUID;

public class CreatePrivateConversationUseCase {

    private final ConversationRepositoryPort conversationRepository;
    private final ConversationDomainService conversationDomainService;
    private final ConversationEventPublisherPort eventPublisher;

    public CreatePrivateConversationUseCase(ConversationRepositoryPort conversationRepository, ConversationDomainService conversationDomainService, ConversationEventPublisherPort eventPublisher) {
        this.conversationRepository = conversationRepository;
        this.conversationDomainService = conversationDomainService;
        this.eventPublisher = eventPublisher;
    }

    public Conversation execute(CreatePrivateConversationCommand command) {

        // Validar que no exista ya una conversación directa entre estos dos
        conversationDomainService.validateDirectConversationNotExists(
                command.getCreatorId(), command.getRecipientId()
        );

        // Crear la conversación
        Conversation conversation = Conversation.create(
                command.getTitle(), ConversationType.DIRECT, command.getCreatorId()
        );

        // Agregar al creador como ADMIN
        Participant creator = new Participant(
                UUID.randomUUID(), conversation.getId(),
                command.getCreatorId(), ParticipantRole.ADMIN,
                command.getCreatorId(), null, false, true
        );

        // Agregar al destinatario como MEMBER
        Participant recipient = new Participant(
                UUID.randomUUID(), conversation.getId(),
                command.getRecipientId(), ParticipantRole.MEMBER,
                command.getCreatorId(), null, false, true
        );

        conversation.addParticipant(creator);
        conversation.addParticipant(recipient);

        // Persistir y retornar
        Conversation saved = conversationRepository.save(conversation);

        // Publicar evento
        eventPublisher.publishConversationCreated(new ConversationCreatedEvent(
                saved.getId(), saved.getType(), saved.getCreatedBy()
        ));

        return saved;
    }
}
