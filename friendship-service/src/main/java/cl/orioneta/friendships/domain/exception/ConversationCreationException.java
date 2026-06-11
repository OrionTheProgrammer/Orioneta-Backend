package cl.orioneta.friendships.domain.exception;

/**
 * Error al abrir el chat privado asociado a una amistad aceptada.
 */
public class ConversationCreationException extends RuntimeException {

    public ConversationCreationException(String message) {
        super(message);
    }
}
