package cl.orioneta.auth.infrastructure.security;

import static org.assertj.core.api.Assertions.assertThat;

import cl.orioneta.auth.app.security.AccessToken;
import cl.orioneta.auth.app.security.TokenClaims;
import cl.orioneta.auth.domain.model.AuthUser;
import cl.orioneta.auth.domain.model.Role;
import cl.orioneta.auth.infrastructure.config.AuthJwtProperties;
import cl.orioneta.auth.infrastructure.config.JwtConfig;
import java.time.Duration;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;

/**
 * Pruebas del emisor/validador de JWT propio de Orioneta.
 */
class JwtTokenManagerTest {

    @Test
    void createsAndValidatesOwnAccessToken() {
        AuthJwtProperties properties = new AuthJwtProperties(
                "orioneta-test",
                "orioneta-test-secret-with-more-than-32-bytes",
                Duration.ofMinutes(15),
                Duration.ofDays(30)
        );
        JwtConfig jwtConfig = new JwtConfig();
        SecretKey secretKey = jwtConfig.authJwtSecretKey(properties);
        JwtEncoder encoder = jwtConfig.jwtEncoder(secretKey);
        JwtDecoder decoder = jwtConfig.jwtDecoder(secretKey);
        JwtTokenManager tokenManager = new JwtTokenManager(properties, encoder, decoder);
        AuthUser user = AuthUser.createLocal("ori@orioneta.cl", "hashed-password", "Ori");

        AccessToken accessToken = tokenManager.createAccessToken(user);
        TokenClaims claims = tokenManager.validateAccessToken(accessToken.value());

        assertThat(claims.userId()).isEqualTo(user.getId());
        assertThat(claims.email()).isEqualTo("ori@orioneta.cl");
        assertThat(claims.role()).isEqualTo(Role.USER);
        assertThat(accessToken.expiresInSeconds()).isEqualTo(900);
    }

    @Test
    void hashesRefreshTokenWithoutKeepingRawValue() {
        AuthJwtProperties properties = new AuthJwtProperties(
                "orioneta-test",
                "orioneta-test-secret-with-more-than-32-bytes",
                Duration.ofMinutes(15),
                Duration.ofDays(30)
        );
        JwtConfig jwtConfig = new JwtConfig();
        SecretKey secretKey = jwtConfig.authJwtSecretKey(properties);
        JwtTokenManager tokenManager = new JwtTokenManager(properties, jwtConfig.jwtEncoder(secretKey), jwtConfig.jwtDecoder(secretKey));

        String rawRefreshToken = tokenManager.createRefreshToken();
        String hash = tokenManager.hashRefreshToken(rawRefreshToken);

        assertThat(rawRefreshToken).isNotBlank();
        assertThat(hash).isNotBlank();
        assertThat(hash).isNotEqualTo(rawRefreshToken);
        assertThat(tokenManager.hashRefreshToken(rawRefreshToken)).isEqualTo(hash);
    }
}
