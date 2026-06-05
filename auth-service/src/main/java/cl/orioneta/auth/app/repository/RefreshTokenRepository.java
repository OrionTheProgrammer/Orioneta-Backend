package cl.orioneta.auth.app.repository;

import cl.orioneta.auth.domain.model.RefreshToken;
import java.util.Optional;

/**
 * Puerto de persistencia para refresh tokens.
 */
public interface RefreshTokenRepository {

    RefreshToken save(RefreshToken refreshToken);

    Optional<RefreshToken> findByTokenHash(String tokenHash);
}
