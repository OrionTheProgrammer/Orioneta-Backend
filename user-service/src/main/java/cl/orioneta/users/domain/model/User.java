package cl.orioneta.users.domain.model;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Representa el perfil publico de un usuario dentro de Orioneta.
 *
 * <p>Esta clase vive en {@code domain} porque contiene reglas propias del
 * negocio: formato de email, largo de textos, generacion de friend code y
 * cambios permitidos del perfil. No depende de Spring, JPA ni HTTP.</p>
 */
public class User {
    private static final Pattern UUID_PATTERN = Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

    private static final int BIO_MAX_LENGTH = 260;
    private static final int MAX_LENGTH = 60;
    private static final int MIN_LENGTH = 3;
    private static final int HEX_CODE_LENGTH = 12;
    private static final int EMAIL_MAX_LENGTH = 120;
    private static final int URL_MAX_LENGTH = 500;


    private UUID userID;
    private String userName;
    private String displayName;
    private String bio;
    private String email;
    private String friendCode;
    private Status status;
    private VisibilityStatus visibility;
    private String profilePhoto;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    /**
     * Crea un usuario nuevo desde datos ingresados por la aplicacion.
     *
     * <p>El id, friend code y fechas se generan dentro del dominio para que la
     * entidad nazca siempre en un estado valido.</p>
     */
    public User(String userName, String displayName, String bio, String email, String status, String visibility, String profilePhoto){
        LocalDateTime now = LocalDateTime.now();

        this.userID = UUID.randomUUID();
        this.userName = stringValidator(userName, MIN_LENGTH, MAX_LENGTH);
        this.displayName = stringValidator(displayName, MIN_LENGTH, MAX_LENGTH);
        this.bio = (bio == null || bio.isBlank()) ? "" : stringValidator(bio, 0, BIO_MAX_LENGTH);
        this.email = emailValidator(email);
        this.friendCode = FriendCode.friendCodeGen(HEX_CODE_LENGTH);
        this.status = statusValidator(status);
        this.visibility = visibilityValidator(visibility);
        this.profilePhoto = optionalStringValidator(profilePhoto, URL_MAX_LENGTH);
        this.createdAt = now;
        this.updatedAt = now;
    }

    private User(String userID, String userName, String displayName, String bio, String email, String friendCode, String status, String visibility, String profilePhoto, LocalDateTime createdAt, LocalDateTime updatedAt){
        this.userID = idValidator(userID);
        this.userName = stringValidator(userName, MIN_LENGTH, MAX_LENGTH);
        this.displayName = stringValidator(displayName, MIN_LENGTH, MAX_LENGTH);
        this.bio = (bio == null || bio.isBlank()) ? "" : stringValidator(bio, 0, BIO_MAX_LENGTH);
        this.email = emailValidator(email);
        this.friendCode = FriendCode.codeValidator(friendCode, HEX_CODE_LENGTH);
        this.status = statusValidator(status);
        this.visibility = visibilityValidator(visibility);
        this.profilePhoto = optionalStringValidator(profilePhoto, URL_MAX_LENGTH);
        this.createdAt = (createdAt == null) ? LocalDateTime.now() : createdAt;
        this.updatedAt = (updatedAt == null) ? this.createdAt : updatedAt;
    }

    /**
     * Reconstruye un usuario que viene desde la base de datos.
     *
     * <p>Se usa un nombre intencionalmente distinto a "constructor normal" para
     * recordar que no estamos creando un usuario nuevo, sino rehidratando uno
     * existente desde persistencia.</p>
     */
    public static User rehidratado(String userID, String userName, String displayName, String bio, String email, String friendCode, String status, String visibility, String profilePhoto){
        return rehidratado(userID, userName, displayName, bio, email, friendCode, status, visibility, profilePhoto, null, null);
    }

    /**
     * Reconstruye un usuario existente conservando sus fechas originales.
     */
    public static User rehidratado(String userID, String userName, String displayName, String bio, String email, String friendCode, String status, String visibility, String profilePhoto, LocalDateTime createdAt, LocalDateTime updatedAt){
        return new User(userID, userName, displayName, bio, email, friendCode, status, visibility, profilePhoto, createdAt, updatedAt);
    }

    /**
     * Actualiza solo los datos editables del perfil.
     *
     * <p>Un valor {@code null} significa "no modificar". Un string vacio en
     * {@code bio} o {@code profilePhoto} sirve para limpiar ese campo.</p>
     */
    public void updateProfile(String displayName, String bio, String profilePhoto) {
        if (displayName != null) {
            this.displayName = stringValidator(displayName, MIN_LENGTH, MAX_LENGTH);
        }

        if (bio != null) {
            this.bio = bio.isBlank() ? "" : stringValidator(bio, 0, BIO_MAX_LENGTH);
        }

        if (profilePhoto != null) {
            this.profilePhoto = optionalStringValidator(profilePhoto, URL_MAX_LENGTH);
        }

        touch();
    }

    /**
     * Cambia el estado de presencia del usuario.
     */
    public void changeStatus(Status status) {
        this.status = (status == null) ? Status.OFFLINE : status;
        touch();
    }

    /**
     * Cambia la visibilidad publica de la cuenta.
     */
    public void changeVisibility(VisibilityStatus visibility) {
        this.visibility = (visibility == null) ? VisibilityStatus.PUBLIC : visibility;
        touch();
    }

    public UUID getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getBio() {
        return bio;
    }

    public String getEmail() {
        return email;
    }

    public String getFriendCode() {
        return friendCode;
    }

    public Status getStatus() {
        return status;
    }

    public VisibilityStatus getVisibility() {
        return visibility;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }


    private UUID idValidator(String code){
        if (code == null || code.isBlank()){
            throw new IllegalArgumentException("El UUID no puede ser null o estar vacio");
        }else if (!UUID_PATTERN.matcher(code).matches()){
            throw new IllegalArgumentException("UUID no valido");
        }

        return UUID.fromString(code);

    }

    private String stringValidator(String name, int min, int max){
        if (name == null || name.trim().isBlank()){
            throw new IllegalArgumentException("El nombre no puede ser null o estar vacio");
        }

        String normalizedName = name.trim();

        if (normalizedName.length() < min || normalizedName.length() > max){
            throw new IllegalArgumentException("No puede tener una logitud menor a "+min+" ni mayor a "+max);
        }

        return normalizedName;
    }

    private String stringValidator(String name){
        if (name == null || name.trim().isBlank()){
            throw new IllegalArgumentException("El nombre no puede ser null o estar vacio");
        }

        return name.trim();
    }

    private String emailValidator(String email){
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

        if (email == null || email.isBlank()){
            throw new IllegalArgumentException("El email no puede ser null ni estar vacio");
        }else if (!email.matches(regex)){
            throw new IllegalArgumentException("El formato del email no es valido");
        }

        String normalizedEmail = email.trim().toLowerCase(Locale.ROOT);

        if (normalizedEmail.length() > EMAIL_MAX_LENGTH) {
            throw new IllegalArgumentException("El email no puede superar los " + EMAIL_MAX_LENGTH + " caracteres");
        }

        return normalizedEmail;
    }

    private String optionalStringValidator(String value, int max) {
        if (value == null || value.isBlank()) {
            return "";
        }

        String normalizedValue = value.trim();

        if (normalizedValue.length() > max) {
            throw new IllegalArgumentException("El texto no puede superar los " + max + " caracteres");
        }

        return normalizedValue;
    }

    private Status statusValidator(String status){
        if (status == null || status.isBlank()){
            return Status.OFFLINE;
        }

        String statusR = status.trim();

        for(Status type: Status.values()){
            if (type.name().equalsIgnoreCase(statusR)){
                return type;
            }
        }

        return Status.OFFLINE;
    }

    private VisibilityStatus visibilityValidator(String visibility){
        if (visibility == null || visibility.isBlank()){
            return VisibilityStatus.PUBLIC;
        }

        String visibilityR = visibility.trim();

        for(VisibilityStatus type: VisibilityStatus.values()){
            if (type.name().equalsIgnoreCase(visibilityR)){
                return type;
            }
        }

        return VisibilityStatus.PUBLIC;
    }

    private void touch() {
        this.updatedAt = LocalDateTime.now();
    }

}
