package cl.orioneta.netamarket.application.dto;

import cl.orioneta.netamarket.domain.model.NetaTemplateStatus;
import cl.orioneta.netamarket.domain.model.NetaTemplateType;

import java.time.LocalDateTime;
import java.util.UUID;

public record NetaTemplateResponseDTO(
        UUID id,
        UUID authorUserId,
        String name,
        String description,
        NetaTemplateType type,
        NetaTemplateStatus status,
        String previewImageUrl,
        String fileUrl,
        String version,
        long downloads,
        double ratingAverage,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
