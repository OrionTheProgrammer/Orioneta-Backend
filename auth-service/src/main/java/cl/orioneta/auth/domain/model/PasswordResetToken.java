package cl.orioneta.auth.domain.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "password_reset_tokens")
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private Instant expiresAt;

    @Column(nullable = false)
    private boolean used = false;

    public static PasswordResetToken create(String email, String code, Instant expiresAt) {
        PasswordResetToken token = new PasswordResetToken();
        token.email = email;
        token.code = code;
        token.expiresAt = expiresAt;
        return token;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }

    public boolean isValid() {
        return !used && !isExpired();
    }

    public void markUsed() {
        this.used = true;
    }

    // getters
    public String getEmail() { return email; }
    public String getCode()  { return code; }
    public boolean isUsed()  { return used; }
}
