package cl.orioneta.friendships.domain.exception;

/**
 * Error para cuando ya existe una solicitud o amistad entre dos usuarios.
 */
public class FriendshipAlreadyExistsException extends RuntimeException {

    public FriendshipAlreadyExistsException(String message) {
        super(message);
    }
}
