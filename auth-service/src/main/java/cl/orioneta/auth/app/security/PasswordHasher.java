package cl.orioneta.auth.app.security;

/**
 * Puerto para hashear y verificar passwords.
 */
public interface PasswordHasher {

    String hash(String rawPassword);

    boolean matches(String rawPassword, String passwordHash);
}
