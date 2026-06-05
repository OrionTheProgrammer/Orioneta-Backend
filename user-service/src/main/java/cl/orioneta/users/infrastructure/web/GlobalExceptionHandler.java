package cl.orioneta.users.infrastructure.web;

import cl.orioneta.users.domain.exception.InvalidUserDataException;
import cl.orioneta.users.domain.exception.UserAlreadyExistsException;
import cl.orioneta.users.domain.exception.UserNotFoundException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Traduce errores Java a respuestas HTTP claras.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse handleUserNotFound(UserNotFoundException exception) {
        return buildError(HttpStatus.NOT_FOUND, exception.getMessage(), Map.of());
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiErrorResponse handleUserAlreadyExists(UserAlreadyExistsException exception) {
        return buildError(HttpStatus.CONFLICT, exception.getMessage(), Map.of());
    }

    @ExceptionHandler({InvalidUserDataException.class, IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleInvalidUserData(RuntimeException exception) {
        return buildError(HttpStatus.BAD_REQUEST, exception.getMessage(), Map.of());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiErrorResponse handleDataIntegrity(DataIntegrityViolationException exception) {
        return buildError(HttpStatus.CONFLICT, "El usuario contiene datos duplicados o invalidos", Map.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleValidationErrors(MethodArgumentNotValidException exception) {
        Map<String, String> fields = new HashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error -> fields.put(error.getField(), error.getDefaultMessage()));

        return buildError(HttpStatus.BAD_REQUEST, "Error de validacion", fields);
    }

    private ApiErrorResponse buildError(HttpStatus status, String message, Map<String, String> fields) {
        return new ApiErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                fields
        );
    }
}
