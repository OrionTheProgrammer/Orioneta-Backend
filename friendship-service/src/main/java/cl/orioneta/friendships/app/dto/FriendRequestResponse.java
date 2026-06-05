package cl.orioneta.friendships.app.dto;

import cl.orioneta.friendships.domain.model.FriendRequestStatus;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Respuesta publica de una solicitud de amistad.
 */
public record FriendRequestResponse(
        UUID id,
        UUID senderUserId,
        UUID receiverUserId,
        FriendRequestStatus status,
        LocalDateTime createdAt,
        LocalDateTime respondedAt
) {
}
