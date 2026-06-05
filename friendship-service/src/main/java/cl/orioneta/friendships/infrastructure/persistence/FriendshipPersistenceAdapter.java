package cl.orioneta.friendships.infrastructure.persistence;

import cl.orioneta.friendships.app.repository.FriendshipRepository;
import cl.orioneta.friendships.domain.model.FriendRequest;
import cl.orioneta.friendships.domain.model.FriendRequestStatus;
import cl.orioneta.friendships.domain.model.Friendship;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

/**
 * Adaptador que conecta el puerto de aplicacion con Spring Data JPA.
 */
@Repository
public class FriendshipPersistenceAdapter implements FriendshipRepository {

    private final JpaFriendRequestRepository friendRequestRepository;
    private final JpaFriendshipRepository friendshipRepository;

    public FriendshipPersistenceAdapter(
            JpaFriendRequestRepository friendRequestRepository,
            JpaFriendshipRepository friendshipRepository
    ) {
        this.friendRequestRepository = friendRequestRepository;
        this.friendshipRepository = friendshipRepository;
    }

    @Override
    public FriendRequest saveRequest(FriendRequest request) {
        return toDomain(friendRequestRepository.save(toEntity(request)));
    }

    @Override
    public Optional<FriendRequest> findRequestById(UUID requestId) {
        return friendRequestRepository.findById(requestId)
                .map(this::toDomain);
    }

    @Override
    public Optional<FriendRequest> findPendingRequestBetween(UUID firstUserId, UUID secondUserId) {
        return friendRequestRepository.findBetweenUsersByStatus(firstUserId, secondUserId, FriendRequestStatus.PENDING)
                .map(this::toDomain);
    }

    @Override
    public List<FriendRequest> findReceivedRequests(UUID userId) {
        return friendRequestRepository.findByReceiverUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<FriendRequest> findSentRequests(UUID userId) {
        return friendRequestRepository.findBySenderUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Friendship saveFriendship(Friendship friendship) {
        return toDomain(friendshipRepository.save(toEntity(friendship)));
    }

    @Override
    public Optional<Friendship> findFriendshipBetween(UUID firstUserId, UUID secondUserId) {
        return friendshipRepository.findBetweenUsers(firstUserId, secondUserId)
                .map(this::toDomain);
    }

    @Override
    public List<Friendship> findFriendshipsByUserId(UUID userId) {
        return friendshipRepository.findByUserId(userId)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    private FriendRequestEntity toEntity(FriendRequest request) {
        return new FriendRequestEntity(
                request.getId(),
                request.getSenderUserId(),
                request.getReceiverUserId(),
                request.getStatus(),
                request.getCreatedAt(),
                request.getRespondedAt()
        );
    }

    private FriendRequest toDomain(FriendRequestEntity entity) {
        return FriendRequest.rehydrate(
                entity.getId(),
                entity.getSenderUserId(),
                entity.getReceiverUserId(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getRespondedAt()
        );
    }

    private FriendshipEntity toEntity(Friendship friendship) {
        return new FriendshipEntity(
                friendship.getId(),
                friendship.getUserId(),
                friendship.getFriendId(),
                friendship.getStatus(),
                friendship.getCreatedAt(),
                friendship.getUpdatedAt()
        );
    }

    private Friendship toDomain(FriendshipEntity entity) {
        return Friendship.rehydrate(
                entity.getId(),
                entity.getUserId(),
                entity.getFriendId(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
