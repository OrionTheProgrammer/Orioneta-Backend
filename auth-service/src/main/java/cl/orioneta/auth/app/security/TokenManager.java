package cl.orioneta.auth.app.security;

import cl.orioneta.auth.domain.model.AuthUser;
import java.time.LocalDateTime;

/**
 * Puerto para emitir y validar tokens propios de Orioneta.
 */
public interface TokenManager {

    AccessToken createAccessToken(AuthUser user);

    String createRefreshToken();

    String hashRefreshToken(String refreshToken);

    LocalDateTime refreshTokenExpiresAt();

    TokenClaims validateAccessToken(String accessToken);
}
