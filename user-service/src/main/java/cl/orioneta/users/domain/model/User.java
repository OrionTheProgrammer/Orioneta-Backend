package cl.orioneta.users.domain.model;

import cl.orioneta.users.domain.exception.InvalidUserDataException;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

/**
 * Representa la identidad publica de un usuario de Orioneta.
 *
 * <p>Esta clase es modelo de dominio, no entidad JPA. Mantiene las reglas que
 * deben cumplirse sin importar si los datos llegan desde HTTP, persistencia o un
 * consumidor de eventos futuro. La infraestructura debe mapear hacia y desde
 * este modelo para que las validaciones de negocio no queden repartidas en
 * controladores o entidades tecnicas.
 */
public class User {

    private static final int MAX_BIO_LENGTH = 160;
    private static final int FRIEND_CODE_LENGTH = 8;

    private final UUID id;
    private final String username;
    private final String email;
    private final String friendCode;
    private final LocalDateTime createdAt;

    private String displayName;
    private String avatarUrl;
    private String bannerUrl;
    private String bio;
    private UserStatus status;
    private AccountVisibility accountVisibility;
    private LocalDateTime updatedAt;

    private User(
            UUID id,
            String username,
            String displayName,
            String email,
            String friendCode,
            String avatarUrl,
            String bannerUrl,
            String bio,
            UserStatus status,
            AccountVisibility accountVisibility,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = Objects.requireNonNull(id, "El id del usuario es obligatorio.");
        this.username = requireText(username, "El username es obligatorio.");
        this.displayName = requireText(displayName, "El nombre visible es obligatorio.");
        this.email = requireText(email, "El email es obligatorio.");
        this.friendCode = validateFriendCode(friendCode);
        this.avatarUrl = normalizeOptionalText(avatarUrl);
        this.bannerUrl = normalizeOptionalText(bannerUrl);
        this.bio = validateBio(bio);
        this.status = defaultStatus(status);
        this.accountVisibility = defaultVisibility(accountVisibility);
        this.createdAt = Objects.requireNonNull(createdAt, "La fecha de creacion es obligatoria.");
        this.updatedAt = Objects.requireNonNull(updatedAt, "La fecha de actualizacion es obligatoria.");
    }

    /**
     * Crea un usuario nuevo con los valores iniciales definidos por el negocio.
     *
     * <p>El caso de uso entrega el {@code friendCode} despues de verificar que
     * sea unico. El dominio aplica defaults seguros: usuario desconectado,
     * visibilidad publica y fechas iguales para creacion y actualizacion.
     *
     * @param username username unico elegido por la persona
     * @param displayName nombre visible dentro de Orioneta
     * @param email correo asociado al perfil publico
     * @param friendCode codigo hexadecimal publico para agregar amigos
     * @param bio biografia publica opcional
     * @return usuario nuevo listo para persistir
     */
    public static User create(
            String username,
            String displayName,
            String email,
            String friendCode,
            String bio
    ) {
        LocalDateTime now = LocalDateTime.now();
        return new User(
                UUID.randomUUID(),
                username,
                displayName,
                email,
                friendCode,
                null,
                null,
                bio,
                UserStatus.OFFLINE,
                AccountVisibility.PUBLIC,
                now,
                now
        );
    }

    /**
     * Reconstruye un usuario que ya existe en persistencia.
     *
     * <p>Este metodo evita regenerar id, friend code o fechas al leer desde la
     * base de datos. Cualquier adaptador de persistencia debe usarlo cuando
     * transforma una entidad JPA al modelo de dominio.
     *
     * @param id id historico del usuario
     * @param username username unico del usuario
     * @param displayName nombre visible guardado
     * @param email correo asociado al perfil
     * @param friendCode codigo publico de amistad
     * @param avatarUrl URL de avatar guardada
     * @param bannerUrl URL de banner guardada
     * @param bio biografia guardada
     * @param status estado de presencia guardado
     * @param accountVisibility visibilidad guardada
     * @param createdAt fecha original de creacion
     * @param updatedAt fecha de ultima actualizacion
     * @return usuario reconstruido desde persistencia
     */
    public static User rehydrate(
            UUID id,
            String username,
            String displayName,
            String email,
            String friendCode,
            String avatarUrl,
            String bannerUrl,
            String bio,
            UserStatus status,
            AccountVisibility accountVisibility,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        return new User(
                id,
                username,
                displayName,
                email,
                friendCode,
                avatarUrl,
                bannerUrl,
                bio,
                status,
                accountVisibility,
                createdAt,
                updatedAt
        );
    }

    /**
     * Actualiza solo los campos publicos editables del perfil.
     *
     * <p>El username, email y friend code quedan fuera porque identifican al
     * usuario en otros flujos. Si el producto permite cambiarlos mas adelante,
     * conviene crear casos de uso dedicados con auditoria y validaciones propias.
     *
     * @param displayName nuevo nombre visible; si viene null no se modifica
     * @param bio nueva biografia; si viene null no se modifica
     * @param avatarUrl nueva URL de avatar; si viene null no se modifica
     * @param bannerUrl nueva URL de banner; si viene null no se modifica
     */
    public void updateProfile(String displayName, String bio, String avatarUrl, String bannerUrl) {
        if (displayName != null) {
            this.displayName = requireText(displayName, "El nombre visible no puede quedar vacio.");
        }
        if (bio != null) {
            this.bio = validateBio(bio);
        }
        if (avatarUrl != null) {
            this.avatarUrl = normalizeOptionalText(avatarUrl);
        }
        if (bannerUrl != null) {
            this.bannerUrl = normalizeOptionalText(bannerUrl);
        }
        touch();
    }

    /**
     * Cambia el estado de presencia del usuario.
     *
     * <p>Publicar eventos de presencia corresponde a una capa de aplicacion o al
     * {@code realtime-service}; el dominio solo mantiene la regla local.
     *
     * @param status nuevo estado de presencia
     */
    public void changeStatus(UserStatus status) {
        this.status = Objects.requireNonNull(status, "El estado del usuario es obligatorio.");
        touch();
    }

    /**
     * Cambia la visibilidad social del perfil.
     *
     * @param accountVisibility nueva regla de visibilidad
     */
    public void changeVisibility(AccountVisibility accountVisibility) {
        this.accountVisibility = Objects.requireNonNull(
                accountVisibility,
                "La visibilidad de la cuenta es obligatoria."
        );
        touch();
    }

    /**
     * Entrega el id interno usado por persistencia y eventos.
     *
     * @return id del usuario
     */
    public UUID getId() {
        return id;
    }

    /**
     * Entrega el username unico elegido por la persona.
     *
     * @return username unico
     */
    public String getUsername() {
        return username;
    }

    /**
     * Entrega el nombre visible del usuario.
     *
     * @return nombre visible
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Entrega el correo vinculado al perfil publico.
     *
     * @return correo del usuario
     */
    public String getEmail() {
        return email;
    }

    /**
     * Entrega el codigo publico para agregar amigos.
     *
     * @return codigo hexadecimal de amistad
     */
    public String getFriendCode() {
        return friendCode;
    }

    /**
     * Entrega la URL del avatar.
     *
     * @return URL de avatar o null si no existe
     */
    public String getAvatarUrl() {
        return avatarUrl;
    }

    /**
     * Entrega la URL del banner.
     *
     * @return URL de banner o null si no existe
     */
    public String getBannerUrl() {
        return bannerUrl;
    }

    /**
     * Entrega la biografia publica.
     *
     * @return biografia, vacia si no se configuro una
     */
    public String getBio() {
        return bio;
    }

    /**
     * Entrega el estado de presencia actual.
     *
     * @return estado del usuario
     */
    public UserStatus getStatus() {
        return status;
    }

    /**
     * Entrega la visibilidad social de la cuenta.
     *
     * @return visibilidad de la cuenta
     */
    public AccountVisibility getAccountVisibility() {
        return accountVisibility;
    }

    /**
     * Entrega la fecha de creacion del perfil.
     *
     * @return fecha de creacion
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Entrega la fecha de ultima actualizacion.
     *
     * @return fecha de ultima actualizacion
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public String toString() {
        return "User{"
                + "id=" + id
                + ", username='" + username + '\''
                + ", displayName='" + displayName + '\''
                + ", email='" + email + '\''
                + ", friendCode='" + friendCode + '\''
                + ", status=" + status
                + ", accountVisibility=" + accountVisibility
                + ", createdAt=" + createdAt
                + ", updatedAt=" + updatedAt
                + '}';
    }

    private void touch() {
        updatedAt = LocalDateTime.now();
    }

    private static UserStatus defaultStatus(UserStatus status) {
        return status == null ? UserStatus.OFFLINE : status;
    }

    private static AccountVisibility defaultVisibility(AccountVisibility visibility) {
        return visibility == null ? AccountVisibility.PUBLIC : visibility;
    }

    private static String requireText(String value, String errorMessage) {
        if (value == null || value.isBlank()) {
            throw new InvalidUserDataException(errorMessage);
        }
        return value.trim();
    }

    private static String normalizeOptionalText(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }

    private static String validateBio(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }
        String normalized = value.trim();
        if (normalized.length() > MAX_BIO_LENGTH) {
            throw new InvalidUserDataException("La biografia no puede superar 160 caracteres.");
        }
        return normalized;
    }

    private static String validateFriendCode(String value) {
        String normalized = requireText(value, "El friend code es obligatorio.").toUpperCase(Locale.ROOT);
        if (!normalized.matches("[0-9A-F]{" + FRIEND_CODE_LENGTH + "}")) {
            throw new InvalidUserDataException("El friend code debe ser hexadecimal y tener 8 caracteres.");
        }
        return normalized;
    }
}
