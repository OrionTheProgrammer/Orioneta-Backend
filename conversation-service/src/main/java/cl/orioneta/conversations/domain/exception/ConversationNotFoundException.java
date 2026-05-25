package cl.orioneta.conversations.domain.exception;

public class ConversationNotFoundException extends RuntimeException {

    public ConversationNotFoundException(String message) {
        super(message);
    }
}
