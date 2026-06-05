package cl.orioneta.users.domain.exception;

/**
 * Error de dominio para datos de usuario que no cumplen las reglas basicas
 * del perfil.
 */
public class InvalidUserDataException extends RuntimeException {

    public InvalidUserDataException(String message) {
        super(message);
    }
}
