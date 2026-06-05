package cl.orioneta.conversations.infrastructure.in.web;

import cl.orioneta.conversations.application.dto.ConversationResponseDTO;
import cl.orioneta.conversations.application.dto.CreateConversationRequestDTO;
import cl.orioneta.conversations.application.mapper.ConversationMapper;
import cl.orioneta.conversations.application.usecase.AddParticipantUseCase;
import cl.orioneta.conversations.application.usecase.CreateGroupConversationUseCase;
import cl.orioneta.conversations.application.usecase.CreatePrivateConversationUseCase;
import cl.orioneta.conversations.application.usecase.FindConversationByIdUseCase;
import cl.orioneta.conversations.application.usecase.FindUserConversationsUseCase;
import cl.orioneta.conversations.domain.model.Conversation;
import cl.orioneta.conversations.domain.model.ConversationType;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * API REST para conversaciones privadas y grupos.
 */
@RestController
@RequestMapping("/api/conversations")
public class ConversationController {

    private final CreatePrivateConversationUseCase createPrivateConversationUseCase;
    private final CreateGroupConversationUseCase createGroupConversationUseCase;
    private final FindConversationByIdUseCase findConversationByIdUseCase;
    private final FindUserConversationsUseCase findUserConversationsUseCase;
    private final AddParticipantUseCase addParticipantUseCase;
    private final ConversationMapper conversationMapper;

    public ConversationController(
            CreatePrivateConversationUseCase createPrivateConversationUseCase,
            CreateGroupConversationUseCase createGroupConversationUseCase,
            FindConversationByIdUseCase findConversationByIdUseCase,
            FindUserConversationsUseCase findUserConversationsUseCase,
            AddParticipantUseCase addParticipantUseCase,
            ConversationMapper conversationMapper
    ) {
        this.createPrivateConversationUseCase = createPrivateConversationUseCase;
        this.createGroupConversationUseCase = createGroupConversationUseCase;
        this.findConversationByIdUseCase = findConversationByIdUseCase;
        this.findUserConversationsUseCase = findUserConversationsUseCase;
        this.addParticipantUseCase = addParticipantUseCase;
        this.conversationMapper = conversationMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ConversationResponseDTO createConversation(@Valid @RequestBody CreateConversationRequestDTO request) {
        Conversation conversation = request.type() == ConversationType.PRIVATE_CHAT
                ? createPrivateConversationUseCase.execute(request)
                : createGroupConversationUseCase.execute(request);

        return conversationMapper.toResponse(conversation);
    }

    @GetMapping("/{id}")
    public ConversationResponseDTO findById(@PathVariable UUID id) {
        return conversationMapper.toResponse(findConversationByIdUseCase.execute(id));
    }

    @GetMapping("/users/{userId}")
    public List<ConversationResponseDTO> findByUserId(@PathVariable UUID userId) {
        return findUserConversationsUseCase.execute(userId)
                .stream()
                .map(conversationMapper::toResponse)
                .toList();
    }

    @PostMapping("/{conversationId}/participants/{userId}")
    public ConversationResponseDTO addParticipant(@PathVariable UUID conversationId, @PathVariable UUID userId) {
        return conversationMapper.toResponse(addParticipantUseCase.execute(conversationId, userId));
    }
}
