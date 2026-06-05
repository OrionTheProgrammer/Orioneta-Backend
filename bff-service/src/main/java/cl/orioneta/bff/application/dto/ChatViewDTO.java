package cl.orioneta.bff.application.dto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Vista agregada de un chat.
 */
public record ChatViewDTO(
        UUID conversationId,
        UUID userId,
        Map<String, Object> conversation,
        List<Map<String, Object>> messages,
        Map<String, Object> customization
) {
}
