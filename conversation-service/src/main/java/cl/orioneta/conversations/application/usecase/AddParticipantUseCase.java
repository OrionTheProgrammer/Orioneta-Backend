package cl.orioneta.conversations.application.usecase;

import cl.orioneta.conversations.application.command.AddParticipantCommand;
import cl.orioneta.conversations.domain.exception.ConversationNotFoundException;
import cl.orioneta.conversations.domain.model.Conversation;
import cl.orioneta.conversations.domain.model.Participant;
import cl.orioneta.conversations.domain.model.ParticipantRole;
import cl.orioneta.conversations.domain.repository.ConversationRepositoryPort;
import cl.orioneta.conversations.domain.service.ConversationDomainService;

import java.util.UUID;

public class AddParticipantUseCase {

    private final ConversationRepositoryPort conversationRepository;
    private final ConversationDomainService conversationDomainService;

    public AddParticipantUseCase(ConversationRepositoryPort conversationRepository, ConversationDomainService conversationDomainService) {
        this.conversationRepository = conversationRepository;
        this.conversationDomainService = conversationDomainService;
    }

    public Conversation execute(AddParticipantCommand command){

        // Buscar conversacion
        Conversation conversation = conversationRepository.findById(command.getConversationId())
                .orElseThrow(() -> new ConversationNotFoundException(command.getConversationId()));

        // Validar que quien solicita es ADMIN
        conversationDomainService.validateCanAddParticipant(conversation, command.getRequestingUserId());

        // Validar limite de participantes si es DIRECT
        conversationDomainService.validateParticipantLimit(conversation);

        // Agregar el nuevo participante
        conversation.addParticipant(new Participant(
                UUID.randomUUID(), conversation.getId(),
                command.getNewUserId(), ParticipantRole.MEMBER,
                command.getRequestingUserId(), null, false, true
        ));

        // Persistir (guardar) y retornar
        return conversationRepository.save(conversation);


    }
}
