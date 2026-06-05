package cl.orioneta.users.infrastructure.web;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Respuesta uniforme para errores HTTP del user-service.
 */
public record ApiErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        Map<String, String> fields
) {
}
