package cl.orioneta.conversations.application.mapper;

import cl.orioneta.conversations.application.dto.ConversationResponseDTO;
import cl.orioneta.conversations.application.dto.ParticipantDTO;
import cl.orioneta.conversations.domain.model.Conversation;
import cl.orioneta.conversations.domain.model.Participant;

import java.util.List;
import java.util.stream.Collectors;

public class ConversationMapper {

    // Conversation dominio a ConversationResponseDTO
    public static ConversationResponseDTO toDTO(Conversation conversation) {
        return new ConversationResponseDTO(
                conversation.getId(),
                conversation.getTitle(),
                conversation.getType(),
                conversation.getAvatarUrl(),
                conversation.getCreatedBy(),
                toParticipantDTOList(conversation.getParticipants()),
                conversation.getCreatedAt(),
                conversation.getUpdatedAt()
        );
    }


    public static List<ConversationResponseDTO> toDTOList(List<Conversation> conversations) {
        return conversations.stream()
                .map(ConversationMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Participant dominio a ParticipantDTO
    public static ParticipantDTO toParticipantDTO(Participant participant) {
        return new ParticipantDTO(
                participant.getId(),
                participant.getUserId(),
                participant.getRole(),
                participant.getLastReadAt(),
                participant.getMuted(),
                participant.getActive()
        );
    }

    //
    public static List<ParticipantDTO> toParticipantDTOList(List<Participant> participants) {
        return participants.stream()
                .map(ConversationMapper::toParticipantDTO)
                .collect(Collectors.toList());
    }
}
