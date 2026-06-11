package cl.orioneta.media.application.storage;

import java.io.InputStream;

/**
 * Contenido binario recuperado desde el almacenamiento.
 */
public record MediaContent(
        InputStream inputStream,
        String contentType,
        long size
) {
}
