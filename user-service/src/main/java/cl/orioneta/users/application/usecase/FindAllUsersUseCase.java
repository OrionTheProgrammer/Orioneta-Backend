package cl.orioneta.users.application.usecase;

import cl.orioneta.users.domain.model.User;
import cl.orioneta.users.domain.repository.UserRepositoryPort;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso para listar usuarios.
 *
 * <p>Es util para el MVP y pruebas manuales. En produccion probablemente se
 * reemplazara por busqueda paginada y filtros de visibilidad.
 */
@Service
public class FindAllUsersUseCase {

    private final UserRepositoryPort userRepositoryPort;

    /**
     * Crea el caso de uso con el puerto de lectura.
     *
     * @param userRepositoryPort puerto usado para listar usuarios
     */
    public FindAllUsersUseCase(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    /**
     * Lista todos los usuarios conocidos por el adaptador actual.
     *
     * @return usuarios existentes
     */
    @Transactional(readOnly = true)
    public List<User> execute() {
        return userRepositoryPort.findAll();
    }
}
