package cl.orioneta.shared.events;

import java.time.Instant;
import java.util.UUID;

public record GroupInvitationEvent(
        UUID conversationId,
        UUID invitedUserId,
        UUID invitedBy,
        Instant occurredAt
) {
}
