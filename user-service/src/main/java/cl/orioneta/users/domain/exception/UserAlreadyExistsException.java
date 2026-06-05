package cl.orioneta.users.domain.exception;

/**
 * Error de dominio para datos publicos que deben ser unicos, como email,
 * username o friend code.
 */
public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
