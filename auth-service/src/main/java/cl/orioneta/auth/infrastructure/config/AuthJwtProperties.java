package cl.orioneta.auth.infrastructure.config;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Propiedades del JWT propio de Orioneta.
 */
@ConfigurationProperties(prefix = "orioneta.auth.jwt")
public record AuthJwtProperties(
        String issuer,
        String secret,
        Duration accessTokenTtl,
        Duration refreshTokenTtl
) {

    public AuthJwtProperties {
        if (issuer == null || issuer.isBlank()) {
            throw new IllegalArgumentException("orioneta.auth.jwt.issuer es obligatorio");
        }

        if (secret == null || secret.isBlank()) {
            throw new IllegalArgumentException("orioneta.auth.jwt.secret es obligatorio");
        }

        if (accessTokenTtl == null || accessTokenTtl.isNegative() || accessTokenTtl.isZero()) {
            throw new IllegalArgumentException("orioneta.auth.jwt.access-token-ttl debe ser positivo");
        }

        if (refreshTokenTtl == null || refreshTokenTtl.isNegative() || refreshTokenTtl.isZero()) {
            throw new IllegalArgumentException("orioneta.auth.jwt.refresh-token-ttl debe ser positivo");
        }
    }
}
