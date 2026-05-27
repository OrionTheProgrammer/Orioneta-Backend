package cl.orioneta.shared.events;

import java.time.Instant;
import java.util.UUID;

public record TemplateSubmittedEvent(
        UUID templateId,
        UUID authorUserId,
        String templateType,
        Instant occurredAt
) {
}
