package cl.orioneta.media.application.storage;

/**
 * Resultado de guardar un archivo en el storage.
 */
public record StoredMediaObject(
        String storageKey,
        String contentType,
        long size
) {
}
