package cl.orioneta.users.domain.exception;

/**
 * Excepcion usada cuando se intenta crear un usuario con datos ya registrados.
 *
 * <p>El caso mas comun es duplicar username, email o friend code. Se separa de
 * las validaciones generales para que la API pueda responder con HTTP 409
 * Conflict y el cliente entienda que debe elegir otro valor.
 */
public class UserAlreadyExistsException extends RuntimeException {

    /**
     * Crea la excepcion con un mensaje de negocio.
     *
     * @param message detalle del dato duplicado
     */
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
