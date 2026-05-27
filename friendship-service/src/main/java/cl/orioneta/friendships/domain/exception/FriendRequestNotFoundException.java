package cl.orioneta.friendships.domain.exception;

public class FriendRequestNotFoundException extends RuntimeException {

    public FriendRequestNotFoundException(String message) {
        super(message);
    }
}
