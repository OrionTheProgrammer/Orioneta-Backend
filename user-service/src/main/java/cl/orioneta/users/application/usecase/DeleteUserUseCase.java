package cl.orioneta.users.application.usecase;

import cl.orioneta.users.domain.exception.UserNotFoundException;
import cl.orioneta.users.domain.repository.UserRepositoryPort;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso para eliminar un usuario.
 *
 * <p>En el MVP se implementa eliminacion fisica. Si mas adelante se requiere
 * recuperacion de cuentas o auditoria estricta, este caso de uso deberia
 * cambiar a eliminacion logica.
 */
@Service
public class DeleteUserUseCase {

    private final UserRepositoryPort userRepositoryPort;

    /**
     * Crea el caso de uso con el puerto de persistencia.
     *
     * @param userRepositoryPort puerto usado para buscar y eliminar usuarios
     */
    public DeleteUserUseCase(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    /**
     * Elimina un usuario si existe.
     *
     * @param id id del usuario
     */
    @Transactional
    public void execute(UUID id) {
        if (userRepositoryPort.findById(id).isEmpty()) {
            throw new UserNotFoundException("Usuario no encontrado.");
        }
        userRepositoryPort.deleteById(id);
    }
}
