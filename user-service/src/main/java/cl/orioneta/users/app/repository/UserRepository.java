package cl.orioneta.users.app.repository;

import cl.orioneta.users.domain.model.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Contrato que la aplicacion necesita para guardar y consultar usuarios.
 *
 * <p>Es un puerto: la capa {@code app} conoce esta interfaz, pero no sabe si
 * por debajo hay JPA, memoria, MongoDB u otro sistema. Esa decision queda en
 * {@code infrastructure}.</p>
 */
public interface UserRepository {

    User save(User user);

    List<User> findAll();

    Optional<User> findById(UUID userID);

    Optional<User> findByUserName(String userName);

    Optional<User> findByEmail(String email);

    Optional<User> findByFriendCode(String friendCode);

    boolean existsByUserName(String userName);

    boolean existsByEmail(String email);

    boolean existsByFriendCode(String friendCode);

    void deleteById(UUID userID);
}
