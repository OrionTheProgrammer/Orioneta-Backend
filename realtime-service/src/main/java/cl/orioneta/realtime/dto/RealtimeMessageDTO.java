package cl.orioneta.realtime.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.Instant;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RealtimeMessageDTO(
        String type,
        String clientEventId,
        UUID conversationId,
        UUID senderId,
        UUID targetUserId,
        UUID messageId,
        String messageType,
        String content,
        String senderName,
        String senderAvatar,
        Instant occurredAt
) {
    public RealtimeMessageDTO withDefaults(UUID fallbackSenderId) {
        return new RealtimeMessageDTO(
                type,
                clientEventId,
                conversationId,
                senderId == null ? fallbackSenderId : senderId,
                targetUserId,
                messageId,
                messageType,
                content,
                senderName,
                senderAvatar,
                occurredAt == null ? Instant.now() : occurredAt
        );
    }
}
