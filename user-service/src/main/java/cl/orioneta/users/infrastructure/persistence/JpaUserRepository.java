package cl.orioneta.users.infrastructure.persistence;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio JPA concreto.
 *
 * <p>Spring Data implementa estos metodos a partir de sus nombres.</p>
 */
public interface JpaUserRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByUserNameIgnoreCase(String userName);

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByFriendCode(String friendCode);

    boolean existsByUserNameIgnoreCase(String userName);

    boolean existsByEmail(String email);

    boolean existsByFriendCode(String friendCode);
}
