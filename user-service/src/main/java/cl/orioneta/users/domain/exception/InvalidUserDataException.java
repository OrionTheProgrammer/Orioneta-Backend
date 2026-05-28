package cl.orioneta.users.domain.exception;

/**
 * Excepcion para datos de usuario que rompen reglas del dominio.
 *
 * <p>Se lanza cuando el modelo recibe valores incompletos o mal formados, como
 * nombres visibles vacios, biografias demasiado largas o friend codes invalidos.
 */
public class InvalidUserDataException extends RuntimeException {

    /**
     * Crea la excepcion con una descripcion legible para capas superiores.
     *
     * @param message regla de dominio incumplida
     */
    public InvalidUserDataException(String message) {
        super(message);
    }
}
