package cl.orioneta.users.infrastructure.in.web;

import cl.orioneta.users.domain.exception.InvalidUserDataException;
import cl.orioneta.users.domain.exception.UserAlreadyExistsException;
import cl.orioneta.users.domain.exception.UserNotFoundException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Traduce excepciones del user-service a respuestas HTTP consistentes.
 *
 * <p>El dominio y los casos de uso lanzan excepciones con mensajes de negocio.
 * Esta clase decide el codigo HTTP y arma un cuerpo de error uniforme para que
 * el frontend y el BFF puedan manejar errores sin leer stack traces.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Responde cuando no existe el usuario solicitado.
     *
     * @param exception excepcion de dominio
     * @return cuerpo de error
     */
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handleUserNotFound(UserNotFoundException exception) {
        return buildError(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    /**
     * Responde cuando username, email o friend code ya existen.
     *
     * @param exception excepcion de duplicidad
     * @return cuerpo de error
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, Object> handleUserAlreadyExists(UserAlreadyExistsException exception) {
        return buildError(HttpStatus.CONFLICT, exception.getMessage());
    }

    /**
     * Responde cuando el dominio recibe datos invalidos.
     *
     * @param exception excepcion de validacion de dominio
     * @return cuerpo de error
     */
    @ExceptionHandler(InvalidUserDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleInvalidUserData(InvalidUserDataException exception) {
        return buildError(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    /**
     * Responde los errores de validacion declarativa de DTOs.
     *
     * @param exception excepcion generada por {@code @Valid}
     * @return cuerpo de error con detalle por campo
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleValidationErrors(MethodArgumentNotValidException exception) {
        Map<String, String> fields = new HashMap<>();
        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error -> fields.put(error.getField(), error.getDefaultMessage()));

        Map<String, Object> response = buildError(HttpStatus.BAD_REQUEST, "Error de validacion.");
        response.put("fields", fields);
        return response;
    }

    private Map<String, Object> buildError(HttpStatus status, String message) {
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", status.value());
        error.put("error", status.getReasonPhrase());
        error.put("message", message);
        return error;
    }
}
