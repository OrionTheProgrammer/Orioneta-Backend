package cl.orioneta.users.application.mapper;

import cl.orioneta.users.application.dto.UserResponseDTO;
import cl.orioneta.users.domain.model.User;
import org.springframework.stereotype.Component;

/**
 * Convierte objetos de dominio de usuario en DTOs de aplicacion.
 *
 * <p>Mantener el mapeo aqui evita filtrar detalles internos del dominio hacia
 * controladores y permite que los adaptadores de persistencia evolucionen sin
 * cambiar las respuestas de la API.
 */
@Component
public class UserMapper {

    /**
     * Convierte un usuario de dominio a la respuesta consumida por REST y BFF.
     *
     * @param user usuario de dominio
     * @return DTO de respuesta
     */
    public UserResponseDTO toResponse(User user) {
        return new UserResponseDTO(
                user.getId(),
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
