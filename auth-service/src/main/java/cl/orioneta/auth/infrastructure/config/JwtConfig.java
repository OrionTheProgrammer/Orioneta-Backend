package cl.orioneta.auth.infrastructure.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

/**
 * Beans para emitir y validar JWT propios de Orioneta.
 */
@Configuration
@EnableConfigurationProperties({AuthJwtProperties.class, PasswordResetProperties.class})
public class JwtConfig {

    @Bean
    public SecretKey authJwtSecretKey(AuthJwtProperties properties) {
        byte[] secretBytes = properties.secret().getBytes(StandardCharsets.UTF_8);

        if (secretBytes.length < 32) {
            throw new IllegalArgumentException("orioneta.auth.jwt.secret debe tener al menos 32 bytes para HS256");
        }

        return new SecretKeySpec(secretBytes, "HmacSHA256");
    }

    @Bean
    public JwtEncoder jwtEncoder(SecretKey authJwtSecretKey) {
        return new NimbusJwtEncoder(new ImmutableSecret<>(authJwtSecretKey));
    }

    @Bean
    public JwtDecoder jwtDecoder(SecretKey authJwtSecretKey) {
        return NimbusJwtDecoder.withSecretKey(authJwtSecretKey)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }
}
