package cl.orioneta.users.application.usecase;

import cl.orioneta.users.application.dto.UpdateUserStatusRequestDTO;
import cl.orioneta.users.domain.exception.UserNotFoundException;
import cl.orioneta.users.domain.model.User;
import cl.orioneta.users.domain.repository.UserRepositoryPort;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso para cambiar el estado de presencia del usuario.
 */
@Service
public class UpdateUserStatusUseCase {

    private final UserRepositoryPort userRepositoryPort;

    /**
     * Crea el caso de uso con el puerto de persistencia.
     *
     * @param userRepositoryPort puerto usado para buscar y guardar usuarios
     */
    public UpdateUserStatusUseCase(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    /**
     * Actualiza el estado de presencia de un usuario.
     *
     * @param id id del usuario
     * @param request nuevo estado solicitado
     * @return usuario actualizado
     */
    @Transactional
    public User execute(UUID id, UpdateUserStatusRequestDTO request) {
        User user = userRepositoryPort.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado."));

        user.changeStatus(request.status());

        return userRepositoryPort.save(user);
    }
}
