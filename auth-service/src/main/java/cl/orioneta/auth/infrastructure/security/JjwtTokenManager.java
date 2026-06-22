package cl.orioneta.auth.infrastructure.security;

import cl.orioneta.auth.app.security.AccessToken;
import cl.orioneta.auth.app.security.TokenClaims;
import cl.orioneta.auth.app.security.TokenManager;
import cl.orioneta.auth.domain.model.AuthUser;
import cl.orioneta.auth.domain.model.Role;
import cl.orioneta.auth.infrastructure.config.AuthJwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.crypto.SecretKey;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * Implementación de TokenManager usando io.jsonwebtoken (jjwt) para generación y validación de JWT.
 * Esta implementación cumple con los requisitos específicos de usar claims personalizados:
 * - userId: UUID del usuario
 * - email: Email del usuario
 * - roles: Lista de roles del usuario
 */
@Component
@Primary
public class JjwtTokenManager implements TokenManager {

    private final SecureRandom secureRandom = new SecureRandom();
    private final AuthJwtProperties properties;
    private final SecretKey signingKey;

    public JjwtTokenManager(AuthJwtProperties properties) {
        this.properties = properties;
        
        // Validar que el secret tenga al menos 32 bytes para HS256
        byte[] secretBytes = properties.secret().getBytes(StandardCharsets.UTF_8);
        if (secretBytes.length < 32) {
            throw new IllegalArgumentException(
                "orioneta.auth.jwt.secret debe tener al menos 32 bytes para HS256"
            );
        }
        
        this.signingKey = Keys.hmacShaKeyFor(secretBytes);
    }

    @Override
    public AccessToken createAccessToken(AuthUser user) {
        Instant now = Instant.now();
        Instant expiration = now.plus(properties.accessTokenTtl());
        
        Claims claims = Jwts.claims()
                .add("userId", user.getId().toString())
                .add("email", user.getEmail())
                .add("roles", List.of(user.getRole().name()))
                .build();
        
        String token = Jwts.builder()
                .issuer(properties.issuer())
                .subject(user.getId().toString())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .claims(claims)
                .signWith(signingKey)
                .compact();
        
        return new AccessToken(token, expiration, properties.accessTokenTtl().getSeconds());
    }

    @Override
    public String createRefreshToken() {
        byte[] bytes = new byte[48];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    @Override
    public String hashRefreshToken(String refreshToken) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(refreshToken.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 no está disponible", exception);
        }
    }

    @Override
    public LocalDateTime refreshTokenExpiresAt() {
        return LocalDateTime.now().plus(properties.refreshTokenTtl());
    }

    @Override
    public TokenClaims validateAccessToken(String accessToken) {
        Claims claims = Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(accessToken)
                .getPayload();

        return new TokenClaims(
                UUID.fromString(claims.get("userId", String.class)),
                claims.get("email", String.class),
                Role.valueOf((String) claims.get("roles", List.class).get(0)),
                claims.getExpiration().toInstant()
        );
    }
}