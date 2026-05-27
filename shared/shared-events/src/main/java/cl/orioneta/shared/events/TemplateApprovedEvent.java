package cl.orioneta.shared.events;

import java.time.Instant;
import java.util.UUID;

public record TemplateApprovedEvent(
        UUID templateId,
        UUID authorUserId,
        UUID reviewerId,
        Instant occurredAt
) {
}
