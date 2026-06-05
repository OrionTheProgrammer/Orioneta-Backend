package cl.orioneta.auth.infrastructure.security;

import cl.orioneta.auth.app.security.AccessToken;
import cl.orioneta.auth.app.security.TokenClaims;
import cl.orioneta.auth.app.security.TokenManager;
import cl.orioneta.auth.domain.model.AuthUser;
import cl.orioneta.auth.domain.model.Role;
import cl.orioneta.auth.infrastructure.config.AuthJwtProperties;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.stereotype.Component;

/**
 * Emite access tokens JWT y refresh tokens opacos de Orioneta.
 */
@Component
public class JwtTokenManager implements TokenManager {

    private final SecureRandom secureRandom = new SecureRandom();
    private final AuthJwtProperties properties;
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    public JwtTokenManager(AuthJwtProperties properties, JwtEncoder jwtEncoder, JwtDecoder jwtDecoder) {
        this.properties = properties;
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    public AccessToken createAccessToken(AuthUser user) {
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plus(properties.accessTokenTtl());

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(properties.issuer())
                .issuedAt(issuedAt)
                .expiresAt(expiresAt)
                .subject(user.getId().toString())
                .claim("email", user.getEmail())
                .claim("role", user.getRole().name())
                .claim("roles", List.of(user.getRole().name()))
                .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).type("JWT").build();
        String token = jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();

        return new AccessToken(token, expiresAt, properties.accessTokenTtl().toSeconds());
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
            throw new IllegalStateException("SHA-256 no esta disponible", exception);
        }
    }

    @Override
    public LocalDateTime refreshTokenExpiresAt() {
        return LocalDateTime.now().plus(properties.refreshTokenTtl());
    }

    @Override
    public TokenClaims validateAccessToken(String accessToken) {
        Jwt jwt = jwtDecoder.decode(accessToken);

        return new TokenClaims(
                UUID.fromString(jwt.getSubject()),
                jwt.getClaimAsString("email"),
                Role.valueOf(jwt.getClaimAsString("role")),
                jwt.getExpiresAt()
        );
    }
}
