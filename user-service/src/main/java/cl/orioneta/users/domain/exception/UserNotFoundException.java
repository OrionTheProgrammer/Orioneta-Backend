package cl.orioneta.users.domain.exception;

/**
 * Excepcion usada cuando no existe el usuario solicitado.
 *
 * <p>Los casos de uso la lanzan al buscar por id o friend code. El controlador
 * global la traduce a HTTP 404.
 */
public class UserNotFoundException extends RuntimeException {

    /**
     * Crea la excepcion con un mensaje de negocio.
     *
     * @param message detalle de la busqueda fallida
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}
