package cl.orioneta.friendships.domain.exception;

/**
 * Error para cuando no existe una amistad esperada.
 */
public class FriendshipNotFoundException extends RuntimeException {

    public FriendshipNotFoundException(String message) {
        super(message);
    }
}
