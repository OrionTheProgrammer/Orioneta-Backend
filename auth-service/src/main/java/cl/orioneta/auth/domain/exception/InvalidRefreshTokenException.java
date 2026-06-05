package cl.orioneta.auth.domain.exception;

/**
 * Error para refresh tokens invalidos, expirados o revocados.
 */
public class InvalidRefreshTokenException extends RuntimeException {

    public InvalidRefreshTokenException(String message) {
        super(message);
    }
}
