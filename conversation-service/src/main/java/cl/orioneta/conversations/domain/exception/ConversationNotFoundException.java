package cl.orioneta.conversations.domain.exception;

import java.util.UUID;

public class ConversationNotFoundException extends RuntimeException {

    // Excepcion por id
    public ConversationNotFoundException(UUID id) {
        super("Conversacion no encontrada con id: " + id);
    }
}
