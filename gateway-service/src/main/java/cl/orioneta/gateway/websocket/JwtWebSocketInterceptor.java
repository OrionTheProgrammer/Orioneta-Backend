package cl.orioneta.gateway.websocket;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Component
public class JwtWebSocketInterceptor {

    private final JwtDecoder jwtDecoder;

    public JwtWebSocketInterceptor(@Value("${orioneta.auth.jwt.secret:orioneta-dev-jwt-secret-change-me-with-32-bytes-minimum}") String jwtSecret) {
        byte[] secretBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        SecretKey secretKey = new SecretKeySpec(secretBytes, "HmacSHA256");
        this.jwtDecoder = NimbusJwtDecoder.withSecretKey(secretKey)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }

    public record ValidationResult(boolean valid, UUID userId, String error) {}

    public ValidationResult validateToken(String token) {
        try {
            Jwt jwt = jwtDecoder.decode(token);
            String subject = jwt.getSubject();

            if (subject == null || subject.isBlank()) {
                return new ValidationResult(false, null, "Token sin subject");
            }

            return new ValidationResult(true, UUID.fromString(subject), null);
        } catch (Exception e) {
            return new ValidationResult(false, null, "Token invalido: " + e.getMessage());
        }
    }

    public UUID extractUserIdFromUri(URI uri) {
        if (uri == null) {
            return null;
        }

        String token = UriComponentsBuilder.fromUri(uri).build().getQueryParams().getFirst("token");
        if (token == null || token.isBlank()) {
            return null;
        }

        ValidationResult result = validateToken(token);
        return result.valid() ? result.userId() : null;
    }
}
