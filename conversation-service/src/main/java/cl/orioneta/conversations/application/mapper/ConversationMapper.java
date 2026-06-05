package cl.orioneta.conversations.application.mapper;

import cl.orioneta.conversations.application.dto.ConversationResponseDTO;
import cl.orioneta.conversations.application.dto.ParticipantDTO;
import cl.orioneta.conversations.domain.model.Conversation;
import cl.orioneta.conversations.domain.model.Participant;
import org.springframework.stereotype.Component;

/**
 * Convierte modelos de dominio en DTOs para la API.
 */
@Component
public class ConversationMapper {

    public ConversationResponseDTO toResponse(Conversation conversation) {
        return new ConversationResponseDTO(
                conversation.getId(),
                conversation.getType(),
                conversation.getName(),
                conversation.getDescription(),
                conversation.getOwnerId(),
                conversation.getAvatarUrl(),
                conversation.getBackgroundUrl(),
                conversation.getParticipants().stream().map(this::toParticipantDTO).toList(),
                conversation.getCreatedAt(),
                conversation.getUpdatedAt()
        );
    }

    private ParticipantDTO toParticipantDTO(Participant participant) {
        return new ParticipantDTO(
                participant.getId(),
                participant.getUserId(),
                participant.getRole(),
                participant.getJoinedAt(),
                participant.isMuted(),
                participant.isDeletedForUser()
        );
    }
}
