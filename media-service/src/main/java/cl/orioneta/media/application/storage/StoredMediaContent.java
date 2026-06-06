package cl.orioneta.media.application.storage;

import java.io.InputStream;

/**
 * Archivo recuperado desde el storage para descargarlo o servirlo por HTTP.
 */
public record StoredMediaContent(
        InputStream content,
        String contentType,
        long size
) {
}
