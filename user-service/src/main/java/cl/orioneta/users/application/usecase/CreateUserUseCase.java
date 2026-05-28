package cl.orioneta.users.application.usecase;

import cl.orioneta.users.application.dto.CreateUserRequestDTO;
import cl.orioneta.users.domain.exception.UserAlreadyExistsException;
import cl.orioneta.users.domain.model.FriendCodeGenerator;
import cl.orioneta.users.domain.model.User;
import cl.orioneta.users.domain.repository.UserRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso encargado de crear perfiles publicos de usuario.
 *
 * <p>Valida unicidad de username y email antes de crear el modelo de dominio.
 * Tambien genera un friend code unico, necesario para que
 * {@code friendship-service} pueda encontrar usuarios sin exponer ids internos.
 */
@Service
public class CreateUserUseCase {

    private final UserRepositoryPort userRepositoryPort;

    /**
     * Crea el caso de uso con el puerto de persistencia.
     *
     * @param userRepositoryPort puerto usado para consultar y guardar usuarios
     */
    public CreateUserUseCase(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    /**
     * Ejecuta la creacion de usuario.
     *
     * @param request datos publicos iniciales del usuario
     * @return usuario creado y guardado
     */
    @Transactional
    public User execute(CreateUserRequestDTO request) {
        if (userRepositoryPort.existsByUsername(request.username())) {
            throw new UserAlreadyExistsException("El username ya esta en uso.");
        }
        if (userRepositoryPort.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException("El email ya esta en uso.");
        }

        User user = User.create(
                request.username(),
                request.displayName(),
                request.email(),
                generateUniqueFriendCode(),
                request.bio()
        );

        return userRepositoryPort.save(user);
    }

    private String generateUniqueFriendCode() {
        String code;
        do {
            code = FriendCodeGenerator.generate();
        } while (userRepositoryPort.existsByFriendCode(code));
        return code;
    }
}
