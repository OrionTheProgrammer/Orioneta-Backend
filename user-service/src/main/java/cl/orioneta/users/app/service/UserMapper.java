package cl.orioneta.users.app.service;

import cl.orioneta.users.app.dto.UserResponse;
import cl.orioneta.users.domain.model.User;
import org.springframework.stereotype.Component;

/**
 * Convierte el modelo de dominio a objetos de respuesta.
 */
@Component
public class UserMapper {

    /**
     * Crea la respuesta publica que consumiran el BFF, friendship-service o el
     * frontend durante el MVP.
     */
    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getUserID(),
                user.getUserName(),
                user.getDisplayName(),
                user.getBio(),
                user.getEmail(),
                user.getFriendCode(),
                user.getStatus(),
                user.getVisibility(),
                user.getProfilePhoto(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
