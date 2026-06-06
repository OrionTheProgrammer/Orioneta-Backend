package cl.orioneta.media.domain.exception;

/**
 * Error tecnico al guardar o recuperar archivos desde el storage externo.
 */
public class MediaStorageException extends RuntimeException {

    public MediaStorageException(String message) {
        super(message);
    }

    public MediaStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
