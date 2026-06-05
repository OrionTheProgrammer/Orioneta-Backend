package cl.orioneta.gateway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Manejo basico de errores del gateway.
 */
@RestControllerAdvice
public class GatewayExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public Map<String, Object> handleGatewayException(Exception exception) {
        return Map.of(
                "timestamp", LocalDateTime.now(),
                "status", HttpStatus.BAD_GATEWAY.value(),
                "error", HttpStatus.BAD_GATEWAY.getReasonPhrase(),
                "message", exception.getMessage() == null ? "Error en gateway" : exception.getMessage()
        );
    }
}
