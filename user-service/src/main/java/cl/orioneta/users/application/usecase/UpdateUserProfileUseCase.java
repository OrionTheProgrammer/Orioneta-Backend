package cl.orioneta.users.application.usecase;

import cl.orioneta.users.application.dto.UpdateUserProfileRequestDTO;
import cl.orioneta.users.domain.exception.UserNotFoundException;
import cl.orioneta.users.domain.model.User;
import cl.orioneta.users.domain.repository.UserRepositoryPort;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso para modificar datos editables del perfil publico.
 */
@Service
public class UpdateUserProfileUseCase {

    private final UserRepositoryPort userRepositoryPort;

    /**
     * Crea el caso de uso con el puerto de persistencia.
     *
     * @param userRepositoryPort puerto usado para buscar y guardar usuarios
     */
    public UpdateUserProfileUseCase(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    /**
     * Actualiza el perfil de un usuario existente.
     *
     * @param id id del usuario
     * @param request datos editables del perfil
     * @return usuario actualizado
     */
    @Transactional
    public User execute(UUID id, UpdateUserProfileRequestDTO request) {
        User user = userRepositoryPort.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado."));

        user.updateProfile(
                request.displayName(),
                request.bio(),
                request.avatarUrl(),
                request.bannerUrl()
        );

        return userRepositoryPort.save(user);
    }
}
