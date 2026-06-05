package cl.orioneta.friendships.domain.exception;

/**
 * Error para cuando friendship-service no puede resolver un usuario externo.
 */
public class UserLookupException extends RuntimeException {

    public UserLookupException(String message) {
        super(message);
    }
}
