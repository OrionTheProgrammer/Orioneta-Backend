package cl.orioneta.friendships.app.dto;

import cl.orioneta.friendships.domain.model.FriendshipStatus;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Respuesta publica de una relacion de amistad.
 */
public record FriendshipResponse(
        UUID id,
        UUID userId,
        UUID friendId,
        UUID conversationId,
        FriendshipStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
