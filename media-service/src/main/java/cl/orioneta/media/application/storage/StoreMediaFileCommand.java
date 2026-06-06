package cl.orioneta.media.application.storage;

import cl.orioneta.media.domain.model.MediaPurpose;

import java.io.InputStream;
import java.util.UUID;

/**
 * Datos binarios y metadata minima necesaria para guardar un archivo en el
 * almacenamiento de objetos.
 */
public record StoreMediaFileCommand(
        UUID mediaId,
        UUID ownerUserId,
        String fileName,
        String contentType,
        long size,
        MediaPurpose purpose,
        InputStream content
) {
}
