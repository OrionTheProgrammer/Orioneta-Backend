package cl.orioneta.auth.domain.exception;

/**
 * Error para correos ya registrados.
 */
public class AuthUserAlreadyExistsException extends RuntimeException {

    public AuthUserAlreadyExistsException(String message) {
        super(message);
    }
}
