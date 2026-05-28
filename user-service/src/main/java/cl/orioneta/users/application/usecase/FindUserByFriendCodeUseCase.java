package cl.orioneta.users.application.usecase;

import cl.orioneta.users.domain.exception.UserNotFoundException;
import cl.orioneta.users.domain.model.User;
import cl.orioneta.users.domain.repository.UserRepositoryPort;
import java.util.Locale;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso para buscar usuarios mediante su codigo publico de amistad.
 */
@Service
public class FindUserByFriendCodeUseCase {

    private final UserRepositoryPort userRepositoryPort;

    /**
     * Crea el caso de uso con el puerto de lectura.
     *
     * @param userRepositoryPort puerto usado para buscar usuarios
     */
    public FindUserByFriendCodeUseCase(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    /**
     * Busca el usuario asociado a un friend code.
     *
     * @param friendCode codigo publico de amistad
     * @return usuario encontrado
     */
    @Transactional(readOnly = true)
    public User execute(String friendCode) {
        return userRepositoryPort.findByFriendCode(friendCode.toUpperCase(Locale.ROOT))
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con ese friend code."));
    }
}
