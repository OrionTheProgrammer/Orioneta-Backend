package cl.orioneta.users.infrastructure.out.persistence;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio Spring Data JPA para la entidad de usuario.
 *
 * <p>Este contrato queda en infraestructura. El dominio y la aplicacion deben
 * depender de {@code UserRepositoryPort}, no directamente de Spring Data.
 */
public interface JpaUserRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByFriendCode(String friendCode);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByFriendCode(String friendCode);
}
