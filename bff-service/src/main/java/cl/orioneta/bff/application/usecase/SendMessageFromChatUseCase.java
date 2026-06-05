package cl.orioneta.bff.application.usecase;

import cl.orioneta.bff.application.dto.SendMessageBffRequestDTO;
import cl.orioneta.bff.infrastructure.out.client.MessageClient;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Reenvia al message-service una solicitud de mensaje proveniente del frontend.
 */
@Service
public class SendMessageFromChatUseCase {

    private final MessageClient messageClient;

    public SendMessageFromChatUseCase(MessageClient messageClient) {
        this.messageClient = messageClient;
    }

    public Map<String, Object> execute(SendMessageBffRequestDTO request) {
        return messageClient.sendMessage(Map.of(
                "conversationId", request.conversationId(),
                "senderId", request.senderId(),
                "content", request.content(),
                "type", request.type() == null || request.type().isBlank() ? "TEXT" : request.type()
        ));
    }
}
