package cl.orioneta.friendships.app.dto;

import java.util.UUID;

/**
 * Vista minima de usuario que friendship-service necesita desde user-service.
 */
public record UserSummary(
        UUID userID,
        String userName,
        String displayName,
        String email,
        String friendCode
) {
}
