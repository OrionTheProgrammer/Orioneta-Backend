package cl.orioneta.media.domain.exception;

public class MediaNotFoundException extends RuntimeException {

    public MediaNotFoundException(String message) {
        super(message);
    }
}
