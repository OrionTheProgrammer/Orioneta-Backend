package cl.orioneta.users.application.dto;

import cl.orioneta.users.domain.model.AccountVisibility;
import cl.orioneta.users.domain.model.UserStatus;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response returned by user-facing endpoints.
 *
 * <p>It exposes the public identity needed by BFF, friendship and conversation
 * flows. Sensitive authentication details never belong here.
 *
 * @param id internal user id
 * @param username unique username
 * @param displayName visible name
 * @param email email associated with the profile
 * @param friendCode public hexadecimal code used to add friends
 * @param avatarUrl optional avatar image URL
 * @param bannerUrl optional profile banner URL
 * @param bio optional biography
 * @param status current presence state
 * @param accountVisibility discoverability setting
 * @param createdAt creation timestamp
 * @param updatedAt last update timestamp
 */
public record UserResponseDTO(
        UUID id,
        String username,
        String displayName,
        String email,
        String friendCode,
        String avatarUrl,
        String bannerUrl,
        String bio,
        UserStatus status,
        AccountVisibility accountVisibility,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
