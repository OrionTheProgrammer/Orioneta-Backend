package cl.orioneta.auth.infrastructure.config;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "orioneta.auth.password-reset")
public record PasswordResetProperties(
        Duration codeTtl,
        String fromEmail,
        String fromName
) {
    public PasswordResetProperties {
        if (codeTtl == null || codeTtl.isNegative() || codeTtl.isZero()) {
            throw new IllegalArgumentException("orioneta.auth.password-reset.code-ttl debe ser positivo");
        }
        if (fromEmail == null || fromEmail.isBlank()) {
            throw new IllegalArgumentException("orioneta.auth.password-reset.from-email es obligatorio");
        }
    }
}
