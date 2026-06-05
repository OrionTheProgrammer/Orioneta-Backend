package cl.orioneta.auth.domain.model;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

/**
 * Cuenta autenticable de Orioneta.
 *
 * <p>Puede nacer como cuenta local con email/password o como cuenta social
 * verificada por Google/GitHub. En ambos casos Orioneta emite sus propios JWT,
 * por lo que las sesiones internas no dependen de tokens externos.</p>
 */
public class AuthUser {

    private static final int EMAIL_MAX_LENGTH = 120;
    private static final int DISPLAY_NAME_MAX_LENGTH = 80;

    private final UUID id;
    private final LocalDateTime createdAt;
    private String email;
    private String passwordHash;
    private String displayName;
    private String avatarUrl;
    private AuthProvider provider;
    private String providerUserId;
    private Role role;
    private boolean enabled;
    private LocalDateTime updatedAt;

    private AuthUser(
            UUID id,
            String email,
            String passwordHash,
            String displayName,
            String avatarUrl,
            AuthProvider provider,
            String providerUserId,
            Role role,
            boolean enabled,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = Objects.requireNonNull(id, "El id del usuario auth es obligatorio");
        this.email = validateEmail(email);
        this.passwordHash = passwordHash;
        this.displayName = validateDisplayName(displayName);
        this.avatarUrl = normalizeOptional(avatarUrl);
        this.provider = Objects.requireNonNull(provider, "El proveedor auth es obligatorio");
        this.providerUserId = normalizeOptional(providerUserId);
        this.role = role == null ? Role.USER : role;
        this.enabled = enabled;
        this.createdAt = Objects.requireNonNull(createdAt, "La fecha de creacion es obligatoria");
        this.updatedAt = updatedAt == null ? createdAt : updatedAt;
        validateProviderState();
    }

    public static AuthUser createLocal(String email, String passwordHash, String displayName) {
        LocalDateTime now = LocalDateTime.now();

        return new AuthUser(
                UUID.randomUUID(),
                email,
                requireText(passwordHash, "El hash de password es obligatorio"),
                displayName,
                null,
                AuthProvider.EMAIL,
                null,
                Role.USER,
                true,
                now,
                now
        );
    }

    public static AuthUser createOAuth(
            AuthProvider provider,
            String providerUserId,
            String email,
            String displayName,
            String avatarUrl
    ) {
        LocalDateTime now = LocalDateTime.now();

        return new AuthUser(
                UUID.randomUUID(),
                email,
                null,
                displayName,
                avatarUrl,
                provider,
                providerUserId,
                Role.USER,
                true,
                now,
                now
        );
    }

    public static AuthUser rehydrate(
            UUID id,
            String email,
            String passwordHash,
            String displayName,
            String avatarUrl,
            AuthProvider provider,
            String providerUserId,
            Role role,
            boolean enabled,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        return new AuthUser(id, email, passwordHash, displayName, avatarUrl, provider, providerUserId, role, enabled, createdAt, updatedAt);
    }

    public void linkOAuthProvider(AuthProvider provider, String providerUserId, String avatarUrl) {
        if (provider == AuthProvider.EMAIL) {
            throw new IllegalArgumentException("No se puede vincular EMAIL como proveedor OAuth2");
        }

        this.provider = Objects.requireNonNull(provider, "El proveedor OAuth2 es obligatorio");
        this.providerUserId = requireText(providerUserId, "El id externo del proveedor es obligatorio");
        this.avatarUrl = normalizeOptional(avatarUrl);
        touch();
    }

    public boolean hasPassword() {
        return passwordHash != null && !passwordHash.isBlank();
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public AuthProvider getProvider() {
        return provider;
    }

    public String getProviderUserId() {
        return providerUserId;
    }

    public Role getRole() {
        return role;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    private void validateProviderState() {
        if (provider == AuthProvider.EMAIL && !hasPassword()) {
            throw new IllegalArgumentException("Una cuenta local necesita password");
        }

        if (provider != AuthProvider.EMAIL && providerUserId.isBlank()) {
            throw new IllegalArgumentException("Una cuenta OAuth2 necesita id externo");
        }
    }

    private String validateEmail(String email) {
        String normalizedEmail = requireText(email, "El email es obligatorio").toLowerCase(Locale.ROOT);
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

        if (!normalizedEmail.matches(regex)) {
            throw new IllegalArgumentException("El email no tiene un formato valido");
        }

        if (normalizedEmail.length() > EMAIL_MAX_LENGTH) {
            throw new IllegalArgumentException("El email no puede superar los " + EMAIL_MAX_LENGTH + " caracteres");
        }

        return normalizedEmail;
    }

    private String validateDisplayName(String displayName) {
        String normalizedDisplayName = requireText(displayName, "El nombre visible es obligatorio");

        if (normalizedDisplayName.length() > DISPLAY_NAME_MAX_LENGTH) {
            throw new IllegalArgumentException("El nombre visible no puede superar los " + DISPLAY_NAME_MAX_LENGTH + " caracteres");
        }

        return normalizedDisplayName;
    }

    private static String normalizeOptional(String value) {
        return value == null ? "" : value.trim();
    }

    private static String requireText(String value, String message) {
        if (value == null || value.trim().isBlank()) {
            throw new IllegalArgumentException(message);
        }

        return value.trim();
    }

    private void touch() {
        updatedAt = LocalDateTime.now();
    }
}
