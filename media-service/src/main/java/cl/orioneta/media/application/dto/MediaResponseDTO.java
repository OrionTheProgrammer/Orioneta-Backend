package cl.orioneta.media.application.dto;

import cl.orioneta.media.domain.model.MediaPurpose;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Respuesta publica de un archivo multimedia.
 */
public record MediaResponseDTO(
        UUID id,
        UUID ownerUserId,
        String fileName,
        String contentType,
        long size,
        String url,
        String storageKey,
        MediaPurpose purpose,
        LocalDateTime createdAt,
        LocalDateTime deletedAt
) {
}
