package cl.orioneta.conversations.infrastructure.in.web;

import cl.orioneta.conversations.application.command.AddParticipantCommand;
import cl.orioneta.conversations.application.command.CreateGroupConversationCommand;
import cl.orioneta.conversations.application.command.CreatePrivateConversationCommand;
import cl.orioneta.conversations.application.dto.ConversationResponseDTO;
import cl.orioneta.conversations.application.dto.CreateConversationRequestDTO;
import cl.orioneta.conversations.application.mapper.ConversationMapper;
import cl.orioneta.conversations.application.query.FindUserConversationsQuery;
import cl.orioneta.conversations.application.usecase.AddParticipantUseCase;
import cl.orioneta.conversations.application.usecase.CreateGroupConversationUseCase;
import cl.orioneta.conversations.application.usecase.CreatePrivateConversationUseCase;
import cl.orioneta.conversations.application.usecase.FindUserConversationsUseCase;
import cl.orioneta.conversations.domain.model.Conversation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/conversations")
public class ConversationController {

    private final CreatePrivateConversationUseCase createPrivateConversationUseCase;
    private final CreateGroupConversationUseCase createGroupConversationUseCase;
    private final AddParticipantUseCase addParticipantUseCase;
    private final FindUserConversationsUseCase findUserConversationsUseCase;

    public ConversationController(CreatePrivateConversationUseCase createPrivateConversationUseCase, CreateGroupConversationUseCase createGroupConversationUseCase, AddParticipantUseCase addParticipantUseCase, FindUserConversationsUseCase findUserConversationsUseCase) {
        this.createPrivateConversationUseCase = createPrivateConversationUseCase;
        this.createGroupConversationUseCase = createGroupConversationUseCase;
        this.addParticipantUseCase = addParticipantUseCase;
        this.findUserConversationsUseCase = findUserConversationsUseCase;
    }

    // POST /api/conversations/direct
    @PostMapping("/direct")
    public ResponseEntity<ConversationResponseDTO> createDirectConversation(
            @RequestBody CreateConversationRequestDTO request,
            @RequestHeader("X-User-Id") UUID creatorId) {

        CreatePrivateConversationCommand command = new CreatePrivateConversationCommand(
                creatorId, request.getRecipientId(), request.getTitle()
        );

        Conversation conversation = createPrivateConversationUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(ConversationMapper.toDTO(conversation));
    }

    // POST /api/conversations/group
    @PostMapping("/group")
    public ResponseEntity<ConversationResponseDTO> createGroupConversation(
            @RequestBody CreateConversationRequestDTO request,
            @RequestHeader("X-User-Id") UUID creatorId) {

        CreateGroupConversationCommand command = new CreateGroupConversationCommand(
                creatorId, request.getTitle(), request.getMemberIds()
        );

        Conversation conversation = createGroupConversationUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(ConversationMapper.toDTO(conversation));
    }

    // POST /api/conversation/{id}/participants
    @PostMapping("/{conversationId}/participants")
    public ResponseEntity<ConversationResponseDTO> addParticipant(
            @PathVariable UUID conversationId,
            @RequestBody UUID newUserId,
            @RequestHeader("X-User-Id") UUID requestingUserId) {

        AddParticipantCommand command = new AddParticipantCommand(
                conversationId, requestingUserId, newUserId
        );

        Conversation conversation = addParticipantUseCase.execute(command);
        return ResponseEntity.ok(ConversationMapper.toDTO(conversation));
    }

    // GET /api/conversations
    @GetMapping
    public ResponseEntity<List<ConversationResponseDTO>> getUserConversations(
            @RequestHeader("X-User-Id") UUID userId) {

        FindUserConversationsQuery query = new FindUserConversationsQuery(userId);
        List<Conversation> conversations = findUserConversationsUseCase.execute(query);
        return ResponseEntity.ok(ConversationMapper.toDTOList(conversations));
    }

}
