package cl.orioneta.bff.application.usecase;

import cl.orioneta.bff.application.dto.CreateChatBffRequestDTO;
import cl.orioneta.bff.infrastructure.out.client.ConversationClient;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Crea una conversacion desde el BFF para que el frontend no dependa del
 * contrato interno completo de conversation-service.
 */
@Service
public class CreateChatUseCase {

    private final ConversationClient conversationClient;

    public CreateChatUseCase(ConversationClient conversationClient) {
        this.conversationClient = conversationClient;
    }

    public Map<String, Object> execute(CreateChatBffRequestDTO request) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("type", request.type());
        payload.put("name", request.name() == null ? "" : request.name());
        payload.put("description", request.description() == null ? "" : request.description());
        payload.put("participantIds", request.participantIds());

        if (request.ownerId() != null) {
            payload.put("ownerId", request.ownerId());
        }

        return conversationClient.createConversation(payload);
    }
}
