package cl.orioneta.users.domain.model;

import cl.orioneta.users.domain.exception.UserGlobalException;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Representa la identidad publica de un usuario de Orioneta.
 *
 * <p>Esta clase es modelo de dominio, no entidad JPA. Por eso mantiene reglas
 * que deben cumplirse sin importar si el usuario llega desde un controlador,
 * un adaptador de base de datos o un evento futuro. Los adaptadores deben
 * convertir hacia y desde este modelo, evitando mover validaciones de negocio
 * a controladores o entidades tecnicas.
 */
public class User {

    private static final int MAX_BIO_LENGTH = 160;
    private static final int FRIEND_CODE_LENGTH = 8;

    private final UserID id;
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

    /**
     * Crea un perfil publico nuevo con los campos editables del registro.
     *
     * <p>El {@code friendCode} se genera en el dominio porque forma parte de la
     * identidad publica que usara {@code friendship-service}. Aun asi, el
     * adaptador de persistencia debe validar unicidad antes de guardar, porque
     * solo la base de datos conoce todos los codigos existentes.
     *
     * @param username nombre de usuario unico elegido por la persona
     * @param displayName nombre visible en chats, grupos y perfil
     * @param email correo asociado al perfil publico
     * @param avatarUrl URL opcional del avatar
     * @param bannerUrl URL opcional del banner
     * @param bio biografia publica corta
     */
    public User(String username, String displayName, String email, String avatarUrl, String bannerUrl, String bio) {
        this(
                new UserID(),
                username,
                displayName,
                email,
                FriendCodeGenerator.generate(),
                avatarUrl,
                bannerUrl,
                bio,
                UserStatus.OFFLINE,
                AccountVisibility.PUBLIC,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    /**
     * Reconstruye un usuario existente desde persistencia.
     *
     * <p>Este metodo se usa cuando un adaptador carga datos ya guardados. A
     * diferencia del constructor publico, aqui no se regeneran el id, el
     * {@code friendCode} ni las fechas, porque esos valores ya pertenecen al
     * historial del usuario.
     *
     * @param id identificador interno del usuario
     * @param username nombre de usuario unico
     * @param displayName nombre visible del perfil
     * @param email correo asociado al perfil
     * @param friendCode codigo publico hexadecimal para agregar amigos
     * @param avatarUrl URL opcional del avatar
     * @param bannerUrl URL opcional del banner
     * @param bio biografia publica corta
     * @param status estado de presencia actual
     * @param accountVisibility regla de visibilidad del perfil
     * @param createdAt fecha de creacion original
     * @param updatedAt fecha de ultima actualizacion
     * @return usuario reconstruido con sus valores historicos
     */
    public static User rehydrate(
            UserID id,
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

    private User(
            UserID id,
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
        this.username = requireText(username, "El username no puede ser nulo, vacio o contener solo espacios.");
        this.displayName = requireText(displayName, "El displayName no puede ser nulo, vacio o contener solo espacios.");
        this.email = requireText(email, "El email no puede ser nulo, vacio o contener solo espacios.");
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
     * Actualiza los campos publicos editables del perfil.
     *
     * <p>El username, email, id y codigo de amistad quedan fuera de este metodo
     * porque son parte de la identidad estable. Si Orioneta permite cambiarlos
     * mas adelante, conviene crear casos de uso dedicados para manejar
     * validaciones, auditoria y sincronizacion con otros servicios.
     *
     * @param displayName nuevo nombre visible
     * @param avatarUrl nueva URL de avatar, o null si se elimina
     * @param bannerUrl nueva URL de banner, o null si se elimina
     * @param bio nueva biografia, null o vacia si se limpia
     */
    public void updateProfile(String displayName, String avatarUrl, String bannerUrl, String bio) {
        this.displayName = requireText(displayName, "El displayName no puede ser nulo, vacio o contener solo espacios.");
        this.avatarUrl = normalizeOptionalText(avatarUrl);
        this.bannerUrl = normalizeOptionalText(bannerUrl);
        this.bio = validateBio(bio);
        touch();
    }

    /**
     * Cambia el estado de presencia del usuario.
     *
     * <p>El metodo solo modifica el estado en memoria. Publicar eventos de
     * presencia corresponde a un caso de uso o a un adaptador de infraestructura,
     * para que el dominio siga sin depender de RabbitMQ, WebSocket o Spring.
     *
     * @param status nuevo estado de presencia
     */
    public void updateStatus(UserStatus status) {
        this.status = Objects.requireNonNull(status, "El estado del usuario es obligatorio.");
        touch();
    }

    /**
     * Cambia si el perfil puede ser encontrado por otros usuarios.
     *
     * @param accountVisibility nueva regla de visibilidad
     */
    public void changeAccountVisibility(AccountVisibility accountVisibility) {
        this.accountVisibility = Objects.requireNonNull(
                accountVisibility,
                "La visibilidad de la cuenta es obligatoria."
        );
        touch();
    }

    /**
     * Entrega el identificador interno usado por persistencia y eventos.
     *
     * @return id inmutable del usuario
     */
    public UserID getId() {
        return id;
    }

    /**
     * Entrega el username unico elegido por el usuario.
     *
     * @return username unico
     */
    public String getUsername() {
        return username;
    }

    /**
     * Entrega el nombre visible mostrado en chats, grupos y perfiles.
     *
     * @return nombre visible
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Entrega el correo vinculado al perfil publico.
     *
     * <p>Este valor conecta la identidad de {@code auth-service} con el perfil
     * publico. No debe exponerse en endpoints publicos si la respuesta no esta
     * pensada para el dueno de la cuenta o para servicios internos.
     *
     * @return correo del perfil
     */
    public String getEmail() {
        return email;
    }

    /**
     * Entrega el codigo hexadecimal usado para agregar amigos.
     *
     * @return codigo de amistad, por ejemplo {@code A91F23C7}
     */
    public String getFriendCode() {
        return friendCode;
    }

    /**
     * Entrega la URL del avatar mostrado en perfiles y chats.
     *
     * @return URL del avatar o null si no existe
     */
    public String getAvatarUrl() {
        return avatarUrl;
    }

    /**
     * Entrega la URL del banner del perfil.
     *
     * @return URL del banner o null si no existe
     */
    public String getBannerUrl() {
        return bannerUrl;
    }

    /**
     * Entrega la biografia publica corta.
     *
     * @return biografia, vacia si el usuario no configuro una
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
     * Entrega la regla de visibilidad de la cuenta.
     *
     * @return visibilidad de la cuenta
     */
    public AccountVisibility getAccountVisibility() {
        return accountVisibility;
    }

    /**
     * Entrega la fecha en que se creo el perfil.
     *
     * @return fecha de creacion
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Entrega la fecha de la ultima actualizacion del perfil.
     *
     * @return fecha de ultima actualizacion
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Entrega una representacion compacta para logs y depuracion.
     *
     * @return representacion legible del usuario
     */
    @Override
    public String toString() {
        return "User{"
                + "id=" + id.getValue()
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
            throw new UserGlobalException(errorMessage);
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
            throw new UserGlobalException("La bio no puede superar " + MAX_BIO_LENGTH + " caracteres.");
        }
        return normalized;
    }

    private static String validateFriendCode(String value) {
        String normalized = requireText(value, "El friendCode es obligatorio.").toUpperCase();
        if (!normalized.matches("[0-9A-F]{" + FRIEND_CODE_LENGTH + "}")) {
            throw new UserGlobalException(
                    "El friendCode debe ser hexadecimal y tener "
                            + FRIEND_CODE_LENGTH
                            + " caracteres."
            );
        }
        return normalized;
    }
}
