package cl.orioneta.audit.domain.exception;

public class AuditEventNotFoundException extends RuntimeException {

    public AuditEventNotFoundException(String message) {
        super(message);
    }
}
