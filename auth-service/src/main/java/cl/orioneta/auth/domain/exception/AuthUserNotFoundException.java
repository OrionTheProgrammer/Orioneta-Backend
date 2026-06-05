package cl.orioneta.auth.domain.exception;

/**
 * Error para cuentas auth inexistentes.
 */
public class AuthUserNotFoundException extends RuntimeException {

    public AuthUserNotFoundException(String message) {
        super(message);
    }
}
