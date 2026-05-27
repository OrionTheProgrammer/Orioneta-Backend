package cl.orioneta.users.domain.exception;

/**
 * Base runtime exception for user-domain invariant violations.
 *
 * <p>Throw this exception when the model receives invalid data, such as blank
 * usernames or malformed friend codes. Controllers should translate it to a
 * client-friendly error response, while domain classes can stay independent from
 * Spring MVC.
 */
public class UserGlobalException extends RuntimeException {

    /**
     * Creates a domain exception with a readable business message.
     *
     * @param message explanation of the violated rule
     */
    public UserGlobalException(String message) {
        super(message);
    }
}
