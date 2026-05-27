package cl.orioneta.shared.events;

import java.time.Instant;
import java.util.UUID;

public record FriendRequestSentEvent(
        UUID requestId,
        UUID senderUserId,
        UUID receiverUserId,
        Instant occurredAt
) {
}
