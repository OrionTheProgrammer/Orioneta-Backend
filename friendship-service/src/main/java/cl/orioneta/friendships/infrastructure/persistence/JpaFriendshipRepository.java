package cl.orioneta.friendships.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repositorio JPA de amistades.
 */
public interface JpaFriendshipRepository extends JpaRepository<FriendshipEntity, UUID> {

    @Query("""
            select friendship
            from FriendshipEntity friendship
            where (friendship.userId = :firstUserId and friendship.friendId = :secondUserId)
               or (friendship.userId = :secondUserId and friendship.friendId = :firstUserId)
            """)
    Optional<FriendshipEntity> findBetweenUsers(
            @Param("firstUserId") UUID firstUserId,
            @Param("secondUserId") UUID secondUserId
    );

    @Query("""
            select friendship
            from FriendshipEntity friendship
            where friendship.userId = :userId
               or friendship.friendId = :userId
            order by friendship.updatedAt desc
            """)
    List<FriendshipEntity> findByUserId(@Param("userId") UUID userId);
}
