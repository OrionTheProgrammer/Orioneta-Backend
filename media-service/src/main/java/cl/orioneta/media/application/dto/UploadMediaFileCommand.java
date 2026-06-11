package cl.orioneta.media.application.dto;

import cl.orioneta.media.domain.model.MediaPurpose;

import java.io.InputStream;
import java.util.UUID;

/**
 * Comando interno para subir un archivo recibido como multipart.
 */
public record UploadMediaFileCommand(
        UUID ownerUserId,
        String fileName,
        String contentType,
        long size,
        MediaPurpose purpose,
        InputStream inputStream
) {
}
