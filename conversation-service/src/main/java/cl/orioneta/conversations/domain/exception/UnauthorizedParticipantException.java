package cl.orioneta.conversations.domain.exception;

public class UnauthorizedParticipantException extends RuntimeException {

    // Excepcion cuando un usuario Participante dentro del grupo no tiene permisos de ADMIN
    public UnauthorizedParticipantException() {
        super("No tienes permisos para realizar esta acción en la conversación");
    }
}