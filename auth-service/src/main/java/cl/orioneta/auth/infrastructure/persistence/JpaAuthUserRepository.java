package cl.orioneta.auth.infrastructure.persistence;

import cl.orioneta.auth.domain.model.AuthProvider;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio Spring Data para cuentas auth.
 */
public interface JpaAuthUserRepository extends JpaRepository<AuthUserEntity, UUID> {

    Optional<AuthUserEntity> findByEmail(String email);

    Optional<AuthUserEntity> findByProviderAndProviderUserId(AuthProvider provider, String providerUserId);

    boolean existsByEmail(String email);
}
