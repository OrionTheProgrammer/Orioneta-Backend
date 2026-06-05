package cl.orioneta.friendships.infrastructure.client;

import java.util.UUID;

/**
 * Respuesta minima esperada desde user-service.
 */
public record UserClientResponse(
        UUID userID,
        String userName,
        String displayName,
        String email,
        String friendCode
) {
}
