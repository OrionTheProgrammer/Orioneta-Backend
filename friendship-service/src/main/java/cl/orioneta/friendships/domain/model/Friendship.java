package cl.orioneta.friendships.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Relacion de amistad entre dos usuarios.
 *
 * <p>La amistad se guarda como par ordenado para evitar duplicados: el usuario
 * con UUID menor queda en {@code userId} y el otro en {@code friendId}.</p>
 */
public class Friendship {

    private final UUID id;
    private final UUID userId;
    private final UUID friendId;
    private final LocalDateTime createdAt;
    private FriendshipStatus status;
    private LocalDateTime updatedAt;

    private Friendship(
            UUID id,
            UUID userId,
            UUID friendId,
            FriendshipStatus status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        validateDifferentUsers(userId, friendId);

        this.id = Objects.requireNonNull(id, "El id de amistad es obligatorio");
        this.userId = Objects.requireNonNull(userId, "El usuario principal es obligatorio");
        this.friendId = Objects.requireNonNull(friendId, "El usuario amigo es obligatorio");
        this.status = Objects.requireNonNull(status, "El estado de amistad es obligatorio");
        this.createdAt = Objects.requireNonNull(createdAt, "La fecha de creacion es obligatoria");
        this.updatedAt = Objects.requireNonNull(updatedAt, "La fecha de actualizacion es obligatoria");
    }

    /**
     * Crea una amistad activa entre dos usuarios.
     */
    public static Friendship create(UUID firstUserId, UUID secondUserId) {
        LocalDateTime now = LocalDateTime.now();
        UUID orderedUserId = firstUserId.compareTo(secondUserId) <= 0 ? firstUserId : secondUserId;
        UUID orderedFriendId = firstUserId.compareTo(secondUserId) <= 0 ? secondUserId : firstUserId;

        return new Friendship(UUID.randomUUID(), orderedUserId, orderedFriendId, FriendshipStatus.ACTIVE, now, now);
    }

    /**
     * Reconstruye una amistad existente desde persistencia.
     */
    public static Friendship rehydrate(
            UUID id,
            UUID userId,
            UUID friendId,
            FriendshipStatus status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        return new Friendship(id, userId, friendId, status, createdAt, updatedAt);
    }

    /**
     * Bloquea la relacion entre usuarios.
     */
    public void block() {
        this.status = FriendshipStatus.BLOCKED;
        touch();
    }

    /**
     * Marca la relacion como removida sin borrar historial.
     */
    public void remove() {
        this.status = FriendshipStatus.REMOVED;
        touch();
    }

    public boolean isActive() {
        return status == FriendshipStatus.ACTIVE;
    }

    public boolean containsUser(UUID targetUserId) {
        return userId.equals(targetUserId) || friendId.equals(targetUserId);
    }

    public UUID otherUserId(UUID targetUserId) {
        if (userId.equals(targetUserId)) {
            return friendId;
        }

        if (friendId.equals(targetUserId)) {
            return userId;
        }

        throw new IllegalArgumentException("El usuario no pertenece a esta amistad.");
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getFriendId() {
        return friendId;
    }

    public FriendshipStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    private void touch() {
        this.updatedAt = LocalDateTime.now();
    }

    private static void validateDifferentUsers(UUID userId, UUID friendId) {
        if (userId == null || friendId == null) {
            throw new IllegalArgumentException("Los usuarios de la amistad son obligatorios.");
        }

        if (userId.equals(friendId)) {
            throw new IllegalArgumentException("Una amistad necesita dos usuarios distintos.");
        }
    }
}
