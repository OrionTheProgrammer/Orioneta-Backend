package cl.orioneta.users.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request used to create the public user profile after registration.
 *
 * <p>The authentication credentials belong to {@code auth-service}. This DTO
 * only contains public identity fields managed by {@code user-service}.
 *
 * @param username unique username selected by the user
 * @param displayName visible name shown in Orioneta
 * @param email email used to connect the auth identity with the public profile
 * @param avatarUrl optional avatar image URL
 * @param bannerUrl optional profile banner URL
 * @param bio optional short biography
 */
public record CreateUserRequestDTO(
        @NotBlank
        @Size(min = 3, max = 25)
        String username,

        @NotBlank
        @Size(min = 2, max = 30)
        String displayName,

        @NotBlank
        @Email
        String email,

        String avatarUrl,
        String bannerUrl,

        @Size(max = 160)
        String bio
) {
}
