package cl.orioneta.users.app.dto;

import cl.orioneta.users.domain.model.Status;
import cl.orioneta.users.domain.model.VisibilityStatus;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Respuesta publica del user-service.
 *
 * <p>Se mantiene separada del modelo de dominio para que el API pueda cambiar
 * sin obligar a tocar las reglas internas del usuario.</p>
 */
public record UserResponse(
        UUID userID,
        String userName,
        String displayName,
        String bio,
        String email,
        String friendCode,
        Status status,
        VisibilityStatus visibility,
        String profilePhoto,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
