package cl.orioneta.friendships.app.repository;

import cl.orioneta.friendships.domain.model.FriendRequest;
import cl.orioneta.friendships.domain.model.Friendship;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Contrato de persistencia que necesita la capa de aplicacion.
 */
public interface FriendshipRepository {

    FriendRequest saveRequest(FriendRequest request);

    Optional<FriendRequest> findRequestById(UUID requestId);

    Optional<FriendRequest> findPendingRequestBetween(UUID firstUserId, UUID secondUserId);

    List<FriendRequest> findReceivedRequests(UUID userId);

    List<FriendRequest> findSentRequests(UUID userId);

    Friendship saveFriendship(Friendship friendship);

    Optional<Friendship> findFriendshipBetween(UUID firstUserId, UUID secondUserId);

    List<Friendship> findFriendshipsByUserId(UUID userId);
}
