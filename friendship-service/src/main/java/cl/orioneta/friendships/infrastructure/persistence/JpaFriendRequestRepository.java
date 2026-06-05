package cl.orioneta.friendships.infrastructure.persistence;

import cl.orioneta.friendships.domain.model.FriendRequestStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repositorio JPA de solicitudes de amistad.
 */
public interface JpaFriendRequestRepository extends JpaRepository<FriendRequestEntity, UUID> {

    @Query("""
            select request
            from FriendRequestEntity request
            where request.status = :status
              and (
                (request.senderUserId = :firstUserId and request.receiverUserId = :secondUserId)
                or
                (request.senderUserId = :secondUserId and request.receiverUserId = :firstUserId)
              )
            """)
    Optional<FriendRequestEntity> findBetweenUsersByStatus(
            @Param("firstUserId") UUID firstUserId,
            @Param("secondUserId") UUID secondUserId,
            @Param("status") FriendRequestStatus status
    );

    List<FriendRequestEntity> findByReceiverUserIdOrderByCreatedAtDesc(UUID receiverUserId);

    List<FriendRequestEntity> findBySenderUserIdOrderByCreatedAtDesc(UUID senderUserId);
}
