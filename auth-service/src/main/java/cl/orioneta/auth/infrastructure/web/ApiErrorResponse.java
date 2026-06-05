package cl.orioneta.auth.infrastructure.web;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Formato unico de error para respuestas REST del auth-service.
 */
public record ApiErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        Map<String, String> fields
) {
}
