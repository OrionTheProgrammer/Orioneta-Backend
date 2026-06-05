package cl.orioneta.messages.infrastructure.in.web;

import cl.orioneta.messages.application.dto.EditMessageRequestDTO;
import cl.orioneta.messages.application.dto.MessageResponseDTO;
import cl.orioneta.messages.application.dto.SendMessageRequestDTO;
import cl.orioneta.messages.application.mapper.MessageMapper;
import cl.orioneta.messages.application.usecase.DeleteMessageUseCase;
import cl.orioneta.messages.application.usecase.EditMessageUseCase;
import cl.orioneta.messages.application.usecase.FindMessagesUseCase;
import cl.orioneta.messages.application.usecase.MarkAsReadUseCase;
import cl.orioneta.messages.application.usecase.SendMessageUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * API REST de mensajes.
 */
@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final SendMessageUseCase sendMessageUseCase;
    private final FindMessagesUseCase findMessagesUseCase;
    private final EditMessageUseCase editMessageUseCase;
    private final MarkAsReadUseCase markAsReadUseCase;
    private final DeleteMessageUseCase deleteMessageUseCase;
    private final MessageMapper messageMapper;

    public MessageController(
            SendMessageUseCase sendMessageUseCase,
            FindMessagesUseCase findMessagesUseCase,
            EditMessageUseCase editMessageUseCase,
            MarkAsReadUseCase markAsReadUseCase,
            DeleteMessageUseCase deleteMessageUseCase,
            MessageMapper messageMapper
    ) {
        this.sendMessageUseCase = sendMessageUseCase;
        this.findMessagesUseCase = findMessagesUseCase;
        this.editMessageUseCase = editMessageUseCase;
        this.markAsReadUseCase = markAsReadUseCase;
        this.deleteMessageUseCase = deleteMessageUseCase;
        this.messageMapper = messageMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MessageResponseDTO sendMessage(@Valid @RequestBody SendMessageRequestDTO request) {
        return messageMapper.toResponse(sendMessageUseCase.execute(request));
    }

    @GetMapping("/conversation/{conversationId}")
    public List<MessageResponseDTO> findByConversation(@PathVariable UUID conversationId) {
        return findMessagesUseCase.execute(conversationId)
                .stream()
                .map(messageMapper::toResponse)
                .toList();
    }

    @PatchMapping("/{id}")
    public MessageResponseDTO editMessage(@PathVariable UUID id, @Valid @RequestBody EditMessageRequestDTO request) {
        return messageMapper.toResponse(editMessageUseCase.execute(id, request));
    }

    @PatchMapping("/{id}/read")
    public MessageResponseDTO markAsRead(@PathVariable UUID id) {
        return messageMapper.toResponse(markAsReadUseCase.execute(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMessage(@PathVariable UUID id) {
        deleteMessageUseCase.execute(id);
    }
}
