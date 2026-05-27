package cl.orioneta.users.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request used to update editable public profile fields.
 *
 * <p>Username, email and friend code are intentionally absent. They are identity
 * fields with stronger consistency rules and should have dedicated use cases if
 * Orioneta supports changing them later.
 *
 * @param displayName new visible name
 * @param avatarUrl optional avatar image URL
 * @param bannerUrl optional profile banner URL
 * @param bio optional short biography
 */
public record UpdateUserRequestDTO(
        @NotBlank
        @Size(min = 2, max = 30)
        String displayName,

        String avatarUrl,
        String bannerUrl,

        @Size(max = 160)
        String bio
) {
}
