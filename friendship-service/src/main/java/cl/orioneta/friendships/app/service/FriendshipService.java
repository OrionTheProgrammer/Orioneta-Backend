package cl.orioneta.friendships.app.service;

import cl.orioneta.friendships.app.client.ConversationDirectory;
import cl.orioneta.friendships.app.client.UserDirectory;
import cl.orioneta.friendships.app.dto.BlockUserRequest;
import cl.orioneta.friendships.app.dto.FriendRequestResponse;
import cl.orioneta.friendships.app.dto.FriendshipResponse;
import cl.orioneta.friendships.app.dto.RespondFriendRequest;
import cl.orioneta.friendships.app.dto.SendFriendRequest;
import cl.orioneta.friendships.app.dto.UserSummary;
import cl.orioneta.friendships.app.event.FriendshipEventPublisher;
import cl.orioneta.friendships.app.repository.FriendshipRepository;
import cl.orioneta.friendships.domain.event.FriendRequestAcceptedEvent;
import cl.orioneta.friendships.domain.event.FriendRequestSentEvent;
import cl.orioneta.friendships.domain.exception.FriendRequestNotFoundException;
import cl.orioneta.friendships.domain.exception.FriendshipAlreadyExistsException;
import cl.orioneta.friendships.domain.exception.FriendshipNotFoundException;
import cl.orioneta.friendships.domain.model.FriendRequest;
import cl.orioneta.friendships.domain.model.Friendship;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio principal de aplicacion para solicitudes y amistades.
 *
 * <p>Coordina validaciones, consulta user-service mediante un puerto y delega
 * las reglas de cambio de estado al dominio.</p>
 */
@Service
public class FriendshipService {

    private final FriendshipRepository friendshipRepository;
    private final UserDirectory userDirectory;
    private final ConversationDirectory conversationDirectory;
    private final FriendshipEventPublisher eventPublisher;
    private final FriendshipMapper friendshipMapper;

    public FriendshipService(
            FriendshipRepository friendshipRepository,
            UserDirectory userDirectory,
            ConversationDirectory conversationDirectory,
            FriendshipEventPublisher eventPublisher,
            FriendshipMapper friendshipMapper
    ) {
        this.friendshipRepository = friendshipRepository;
        this.userDirectory = userDirectory;
        this.conversationDirectory = conversationDirectory;
        this.eventPublisher = eventPublisher;
        this.friendshipMapper = friendshipMapper;
    }

    @Transactional
    public FriendRequestResponse sendFriendRequest(SendFriendRequest request) {
        UserSummary sender = userDirectory.findById(request.senderUserId());
        UserSummary receiver = resolveReceiver(request);

        validateNotSameUser(sender.userID(), receiver.userID());
        validateNoActiveFriendship(sender.userID(), receiver.userID());
        validateNoPendingRequest(sender.userID(), receiver.userID());

        FriendRequest friendRequest = FriendRequest.create(sender.userID(), receiver.userID());
        FriendRequest savedRequest = friendshipRepository.saveRequest(friendRequest);

        eventPublisher.publishFriendRequestSent(new FriendRequestSentEvent(
                savedRequest.getId(),
                savedRequest.getSenderUserId(),
                savedRequest.getReceiverUserId(),
                savedRequest.getCreatedAt()
        ));

        return friendshipMapper.toRequestResponse(savedRequest);
    }

    @Transactional
    public FriendshipResponse acceptRequest(UUID requestId, RespondFriendRequest request) {
        FriendRequest friendRequest = findRequestOrFail(requestId);
        friendRequest.accept(request.requesterUserId());

        UUID conversationId = conversationDirectory.createPrivateConversation(
                friendRequest.getSenderUserId(),
                friendRequest.getReceiverUserId()
        );
        Friendship friendship = Friendship.create(
                friendRequest.getSenderUserId(),
                friendRequest.getReceiverUserId(),
                conversationId
        );
        Friendship savedFriendship = friendshipRepository.saveFriendship(friendship);
        friendshipRepository.saveRequest(friendRequest);

        eventPublisher.publishFriendRequestAccepted(new FriendRequestAcceptedEvent(
                friendRequest.getId(),
                savedFriendship.getId(),
                friendRequest.getSenderUserId(),
                friendRequest.getReceiverUserId(),
                LocalDateTime.now()
        ));

        return friendshipMapper.toFriendshipResponse(savedFriendship);
    }

    @Transactional
    public FriendRequestResponse rejectRequest(UUID requestId, RespondFriendRequest request) {
        FriendRequest friendRequest = findRequestOrFail(requestId);
        friendRequest.reject(request.requesterUserId());

        return friendshipMapper.toRequestResponse(friendshipRepository.saveRequest(friendRequest));
    }

    @Transactional
    public FriendRequestResponse cancelRequest(UUID requestId, RespondFriendRequest request) {
        FriendRequest friendRequest = findRequestOrFail(requestId);
        friendRequest.cancel(request.requesterUserId());

        return friendshipMapper.toRequestResponse(friendshipRepository.saveRequest(friendRequest));
    }

    @Transactional(readOnly = true)
    public List<FriendRequestResponse> listReceivedRequests(UUID userId) {
        return friendshipRepository.findReceivedRequests(userId)
                .stream()
                .map(friendshipMapper::toRequestResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<FriendRequestResponse> listSentRequests(UUID userId) {
        return friendshipRepository.findSentRequests(userId)
                .stream()
                .map(friendshipMapper::toRequestResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<FriendshipResponse> listFriends(UUID userId) {
        return friendshipRepository.findFriendshipsByUserId(userId)
                .stream()
                .filter(Friendship::isActive)
                .map(friendshipMapper::toFriendshipResponse)
                .toList();
    }

    @Transactional
    public FriendshipResponse removeFriend(UUID userId, UUID friendId) {
        Friendship friendship = findFriendshipOrFail(userId, friendId);
        friendship.remove();

        return friendshipMapper.toFriendshipResponse(friendshipRepository.saveFriendship(friendship));
    }

    @Transactional
    public FriendshipResponse blockUser(BlockUserRequest request) {
        validateNotSameUser(request.userId(), request.blockedUserId());

        Friendship friendship = friendshipRepository.findFriendshipBetween(request.userId(), request.blockedUserId())
                .orElseGet(() -> Friendship.create(request.userId(), request.blockedUserId()));

        friendship.block();

        return friendshipMapper.toFriendshipResponse(friendshipRepository.saveFriendship(friendship));
    }

    private FriendRequest findRequestOrFail(UUID requestId) {
        return friendshipRepository.findRequestById(requestId)
                .orElseThrow(() -> new FriendRequestNotFoundException("Solicitud de amistad no encontrada"));
    }

    private Friendship findFriendshipOrFail(UUID userId, UUID friendId) {
        return friendshipRepository.findFriendshipBetween(userId, friendId)
                .orElseThrow(() -> new FriendshipNotFoundException("Amistad no encontrada"));
    }

    private UserSummary resolveReceiver(SendFriendRequest request) {
        if (request.receiverUserId() != null) {
            return userDirectory.findById(request.receiverUserId());
        }

        if (request.receiverEmail() != null && !request.receiverEmail().isBlank()) {
            return userDirectory.findByEmail(request.receiverEmail());
        }

        if (request.receiverFriendCode() != null && !request.receiverFriendCode().isBlank()) {
            return userDirectory.findByFriendCode(request.receiverFriendCode());
        }

        throw new IllegalArgumentException("Debes indicar receiverUserId, receiverEmail o receiverFriendCode");
    }

    private void validateNotSameUser(UUID senderUserId, UUID receiverUserId) {
        if (senderUserId.equals(receiverUserId)) {
            throw new IllegalArgumentException("No puedes crear una amistad contigo mismo");
        }
    }

    private void validateNoActiveFriendship(UUID firstUserId, UUID secondUserId) {
        friendshipRepository.findFriendshipBetween(firstUserId, secondUserId)
                .filter(Friendship::isActive)
                .ifPresent(friendship -> {
                    throw new FriendshipAlreadyExistsException("Ya existe una amistad activa entre estos usuarios");
                });
    }

    private void validateNoPendingRequest(UUID firstUserId, UUID secondUserId) {
        friendshipRepository.findPendingRequestBetween(firstUserId, secondUserId)
                .filter(FriendRequest::isPending)
                .ifPresent(request -> {
                    throw new FriendshipAlreadyExistsException("Ya existe una solicitud pendiente entre estos usuarios");
                });
    }
}
