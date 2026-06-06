package cl.orioneta.bff.infrastructure.in.web;

import cl.orioneta.bff.application.dto.ChatViewDTO;
import cl.orioneta.bff.application.dto.CreateChatBffRequestDTO;
import cl.orioneta.bff.application.dto.HomeViewDTO;
import cl.orioneta.bff.application.dto.SendMessageBffRequestDTO;
import cl.orioneta.bff.application.usecase.CreateChatUseCase;
import cl.orioneta.bff.application.usecase.GetChatViewUseCase;
import cl.orioneta.bff.application.usecase.GetHomeViewUseCase;
import cl.orioneta.bff.application.usecase.SendMessageFromChatUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

/**
 * Endpoints agregadores para que el frontend no tenga que llamar a todos los
 * microservicios directamente.
 */
@RestController
@RequestMapping("/api/bff")
public class BffController {

    private final GetHomeViewUseCase getHomeViewUseCase;
    private final GetChatViewUseCase getChatViewUseCase;
    private final CreateChatUseCase createChatUseCase;
    private final SendMessageFromChatUseCase sendMessageFromChatUseCase;

    public BffController(
            GetHomeViewUseCase getHomeViewUseCase,
            GetChatViewUseCase getChatViewUseCase,
            CreateChatUseCase createChatUseCase,
            SendMessageFromChatUseCase sendMessageFromChatUseCase
    ) {
        this.getHomeViewUseCase = getHomeViewUseCase;
        this.getChatViewUseCase = getChatViewUseCase;
        this.createChatUseCase = createChatUseCase;
        this.sendMessageFromChatUseCase = sendMessageFromChatUseCase;
    }

    @GetMapping("/home/{userId}")
    public HomeViewDTO getHome(@PathVariable UUID userId) {
        return getHomeViewUseCase.execute(userId);
    }

    @GetMapping("/chats/{conversationId}")
    public ChatViewDTO getChat(@PathVariable UUID conversationId, @RequestParam UUID userId) {
        return getChatViewUseCase.execute(conversationId, userId);
    }

    @PostMapping("/chats")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> createChat(@Valid @RequestBody CreateChatBffRequestDTO request) {
        return createChatUseCase.execute(request);
    }

    @PostMapping("/chats/messages")
    public Map<String, Object> sendMessage(@Valid @RequestBody SendMessageBffRequestDTO request) {
        return sendMessageFromChatUseCase.execute(request);
    }
}
