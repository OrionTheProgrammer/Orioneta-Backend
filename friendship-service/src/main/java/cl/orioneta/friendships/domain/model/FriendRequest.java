package cl.orioneta.friendships.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Solicitud de amistad entre dos usuarios de Orioneta.
 *
 * <p>Esta clase contiene la regla pura: una solicitud parte pendiente y solo
 * puede aceptarse, rechazarse o cancelarse mientras siga en ese estado.</p>
 */
public class FriendRequest {

    private final UUID id;
    private final UUID senderUserId;
    private final UUID receiverUserId;
    private final LocalDateTime createdAt;
    private FriendRequestStatus status;
    private LocalDateTime respondedAt;

    private FriendRequest(
            UUID id,
            UUID senderUserId,
            UUID receiverUserId,
            FriendRequestStatus status,
            LocalDateTime createdAt,
            LocalDateTime respondedAt
    ) {
        validateDifferentUsers(senderUserId, receiverUserId);

        this.id = Objects.requireNonNull(id, "El id de la solicitud es obligatorio");
        this.senderUserId = Objects.requireNonNull(senderUserId, "El usuario emisor es obligatorio");
        this.receiverUserId = Objects.requireNonNull(receiverUserId, "El usuario receptor es obligatorio");
        this.status = Objects.requireNonNull(status, "El estado de la solicitud es obligatorio");
        this.createdAt = Objects.requireNonNull(createdAt, "La fecha de creacion es obligatoria");
        this.respondedAt = respondedAt;
    }

    /**
     * Crea una solicitud nueva en estado pendiente.
     */
    public static FriendRequest create(UUID senderUserId, UUID receiverUserId) {
        return new FriendRequest(
                UUID.randomUUID(),
                senderUserId,
                receiverUserId,
                FriendRequestStatus.PENDING,
                LocalDateTime.now(),
                null
        );
    }

    /**
     * Reconstruye una solicitud existente desde persistencia.
     */
    public static FriendRequest rehydrate(
            UUID id,
            UUID senderUserId,
            UUID receiverUserId,
            FriendRequestStatus status,
            LocalDateTime createdAt,
            LocalDateTime respondedAt
    ) {
        return new FriendRequest(id, senderUserId, receiverUserId, status, createdAt, respondedAt);
    }

    /**
     * Acepta una solicitud pendiente.
     */
    public void accept(UUID receiverUserId) {
        if (this.status != FriendRequestStatus.PENDING) {
            throw new IllegalStateException("Solo se pueden aceptar solicitudes de amistad pendientes.");
        }

        if (!this.receiverUserId.equals(receiverUserId)) {
            throw new IllegalStateException("Solo el receptor puede aceptar la solicitud de amistad.");
        }

        this.status = FriendRequestStatus.ACCEPTED;
        this.respondedAt = LocalDateTime.now();
    }

    /**
     * Rechaza una solicitud pendiente.
     */
    public void reject(UUID receiverUserId) {
        if (this.status != FriendRequestStatus.PENDING) {
            throw new IllegalStateException("Solo se pueden rechazar solicitudes de amistad pendientes.");
        }

        if (!this.receiverUserId.equals(receiverUserId)) {
            throw new IllegalStateException("Solo el receptor puede rechazar la solicitud de amistad.");
        }

        this.status = FriendRequestStatus.REJECTED;
        this.respondedAt = LocalDateTime.now();
    }

    /**
     * Cancela una solicitud pendiente.
     */
    public void cancel(UUID requesterUserId) {
        if (this.status != FriendRequestStatus.PENDING) {
            throw new IllegalStateException("Solo se pueden cancelar solicitudes de amistad pendientes.");
        }
        if (!this.senderUserId.equals(requesterUserId)) {
            throw new IllegalStateException("Solo el remitente puede cancelar la solicitud de amistad.");
        }

        this.status = FriendRequestStatus.CANCELLED;
        this.respondedAt = LocalDateTime.now();
    }

    public boolean isPending() {
        return status == FriendRequestStatus.PENDING;
    }

    public UUID getId() {
        return id;
    }

    public UUID getSenderUserId() {
        return senderUserId;
    }

    public UUID getReceiverUserId() {
        return receiverUserId;
    }

    public FriendRequestStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getRespondedAt() {
        return respondedAt;
    }

    private static void validateDifferentUsers(UUID senderUserId, UUID receiverUserId) {
        if (senderUserId == null || receiverUserId == null) {
            throw new IllegalArgumentException("Los usuarios de la solicitud son obligatorios.");
        }

        if (senderUserId.equals(receiverUserId)) {
            throw new IllegalArgumentException("No puedes enviarte una solicitud de amistad a ti mismo.");
        }
    }
}
