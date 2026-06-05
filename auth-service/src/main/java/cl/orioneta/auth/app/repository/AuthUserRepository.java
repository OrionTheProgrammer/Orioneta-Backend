package cl.orioneta.auth.app.repository;

import cl.orioneta.auth.domain.model.AuthProvider;
import cl.orioneta.auth.domain.model.AuthUser;
import java.util.Optional;
import java.util.UUID;

/**
 * Puerto de persistencia para usuarios autenticables.
 */
public interface AuthUserRepository {

    AuthUser save(AuthUser user);

    Optional<AuthUser> findById(UUID id);

    Optional<AuthUser> findByEmail(String email);

    Optional<AuthUser> findByProviderAndProviderUserId(AuthProvider provider, String providerUserId);

    boolean existsByEmail(String email);
}
