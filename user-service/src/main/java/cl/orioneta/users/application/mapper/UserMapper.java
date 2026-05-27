package cl.orioneta.users.application.mapper;

import cl.orioneta.users.application.dto.UserResponseDTO;
import cl.orioneta.users.domain.model.User;

/**
 * Converts user domain objects into application DTOs.
 *
 * <p>Keeping mapping here avoids leaking domain internals to controllers and
 * lets future persistence adapters evolve independently from API responses.
 */
public class UserMapper {

    /**
     * Converts a domain user to the response consumed by REST and BFF layers.
     *
     * @param user domain user
     * @return response DTO
     */
    public UserResponseDTO toResponse(User user) {
        return new UserResponseDTO(
                user.getId().getValue(),
                user.getUsername(),
                user.getDisplayName(),
                user.getEmail(),
                user.getFriendCode(),
                user.getAvatarUrl(),
                user.getBannerUrl(),
                user.getBio(),
                user.getStatus(),
                user.getAccountVisibility(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
