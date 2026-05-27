package cl.orioneta.shared.events;

import java.time.Instant;
import java.util.UUID;

public record FriendRequestAcceptedEvent(
        UUID requestId,
        UUID senderUserId,
        UUID receiverUserId,
        Instant occurredAt
) {
}
