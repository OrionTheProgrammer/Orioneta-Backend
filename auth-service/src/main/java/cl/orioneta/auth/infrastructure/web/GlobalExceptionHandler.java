package cl.orioneta.auth.infrastructure.web;

import cl.orioneta.auth.domain.exception.AuthUserAlreadyExistsException;
import cl.orioneta.auth.domain.exception.AuthUserNotFoundException;
import cl.orioneta.auth.domain.exception.InvalidCredentialsException;
import cl.orioneta.auth.domain.exception.InvalidRefreshTokenException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Traduce errores internos a respuestas HTTP consistentes.
 *
 * <p>Manejo específico de errores según arquitectura JWT:
 * - BadCredentialsException: HTTP 401 Unauthorized con JSON limpio
 * - Validaciones: HTTP 400 Bad Request con detalles de campos
 * - Excepciones de dominio: Mapeadas a códigos HTTP apropiados</p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiErrorResponse handleBadCredentials(BadCredentialsException exception) {
        return buildError(
                HttpStatus.UNAUTHORIZED,
                "Credenciales inválidas",
                Map.of("error", "email o contraseña incorrectos")
        );
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiErrorResponse handleInvalidCredentials(InvalidCredentialsException exception) {
        return buildError(
                HttpStatus.UNAUTHORIZED,
                exception.getMessage(),
                Map.of("error", "credenciales inválidas")
        );
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiErrorResponse handleInvalidRefreshToken(InvalidRefreshTokenException exception) {
        return buildError(
                HttpStatus.UNAUTHORIZED,
                exception.getMessage(),
                Map.of("error", "refresh token inválido o expirado")
        );
    }

    @ExceptionHandler(JwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiErrorResponse handleJwtException(JwtException exception) {
        return buildError(
                HttpStatus.UNAUTHORIZED,
                "Token inválido o expirado",
                Map.of("error", exception.getMessage())
        );
    }

    @ExceptionHandler(AuthUserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiErrorResponse handleAlreadyExists(AuthUserAlreadyExistsException exception) {
        return buildError(
                HttpStatus.CONFLICT,
                exception.getMessage(),
                Map.of("error", "el email ya está registrado")
        );
    }

    @ExceptionHandler(AuthUserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse handleNotFound(AuthUserNotFoundException exception) {
        return buildError(
                HttpStatus.NOT_FOUND,
                exception.getMessage(),
                Map.of("error", "usuario no encontrado")
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleBadRequest(IllegalArgumentException exception) {
        return buildError(
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                Map.of("error", "solicitud inválida")
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleValidation(MethodArgumentNotValidException exception) {
        Map<String, String> fields = new HashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error -> fields.put(error.getField(), error.getDefaultMessage()));

        return buildError(
                HttpStatus.BAD_REQUEST,
                "Error de validación",
                Map.of("errors", fields.toString())
        );
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
