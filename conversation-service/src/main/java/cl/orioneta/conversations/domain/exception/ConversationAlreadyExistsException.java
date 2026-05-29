package cl.orioneta.conversations.domain.exception;

public class ConversationAlreadyExistsException extends RuntimeException {

    // excepcion si la tiene una conversacion existente entre usuario DIRECTOS
    public ConversationAlreadyExistsException() {
        super("Ya existe una conversación directa entre estos usuarios");
    }
}