package cl.orioneta.friendships.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Evento publicado cuando una solicitud de amistad es aceptada.
 */
public record FriendRequestAcceptedEvent(
        UUID requestId,
        UUID friendshipId,
        UUID senderUserId,
        UUID receiverUserId,
        LocalDateTime acceptedAt
) {
}
