package cl.orioneta.users.domain.exception;

/**
 * Error de dominio para cuando se intenta trabajar con un usuario inexistente.
 */
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }
}
