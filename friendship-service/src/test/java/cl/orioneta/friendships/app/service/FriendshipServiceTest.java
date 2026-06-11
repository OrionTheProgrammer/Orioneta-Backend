package cl.orioneta.friendships.app.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cl.orioneta.friendships.app.client.ConversationDirectory;
import cl.orioneta.friendships.app.client.UserDirectory;
import cl.orioneta.friendships.app.dto.FriendRequestResponse;
import cl.orioneta.friendships.app.dto.FriendshipResponse;
import cl.orioneta.friendships.app.dto.RespondFriendRequest;
import cl.orioneta.friendships.app.dto.SendFriendRequest;
import cl.orioneta.friendships.app.dto.UserSummary;
import cl.orioneta.friendships.app.event.FriendshipEventPublisher;
import cl.orioneta.friendships.app.repository.FriendshipRepository;
import cl.orioneta.friendships.domain.event.FriendRequestAcceptedEvent;
import cl.orioneta.friendships.domain.event.FriendRequestSentEvent;
import cl.orioneta.friendships.domain.exception.FriendshipAlreadyExistsException;
import cl.orioneta.friendships.domain.model.FriendRequest;
import cl.orioneta.friendships.domain.model.FriendRequestStatus;
import cl.orioneta.friendships.domain.model.Friendship;
import cl.orioneta.friendships.domain.model.FriendshipStatus;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

class FriendshipServiceTest {

    private final Faker faker = new Faker();

    private FriendshipRepository friendshipRepository;
    private UserDirectory userDirectory;
    private ConversationDirectory conversationDirectory;
    private FriendshipEventPublisher eventPublisher;
    private FriendshipService friendshipService;

    @BeforeEach
    void setUp() {
        friendshipRepository = Mockito.mock(FriendshipRepository.class);
        userDirectory = Mockito.mock(UserDirectory.class);
        conversationDirectory = Mockito.mock(ConversationDirectory.class);
        eventPublisher = Mockito.mock(FriendshipEventPublisher.class);
        friendshipService = new FriendshipService(
                friendshipRepository,
                userDirectory,
                conversationDirectory,
                eventPublisher,
                new FriendshipMapper()
        );
    }

    @Test
    void sendsFriendRequestByEmail() {
        UserSummary sender = fakeUser();
        UserSummary receiver = fakeUser();

        when(userDirectory.findById(sender.userID())).thenReturn(sender);
        when(userDirectory.findByEmail(receiver.email())).thenReturn(receiver);
        when(friendshipRepository.findFriendshipBetween(sender.userID(), receiver.userID())).thenReturn(Optional.empty());
        when(friendshipRepository.findPendingRequestBetween(sender.userID(), receiver.userID())).thenReturn(Optional.empty());
        when(friendshipRepository.saveRequest(any(FriendRequest.class))).thenAnswer(invocation -> invocation.getArgument(0));

        FriendRequestResponse response = friendshipService.sendFriendRequest(new SendFriendRequest(
                sender.userID(),
                null,
                receiver.email(),
                null
        ));

        assertThat(response.senderUserId()).isEqualTo(sender.userID());
        assertThat(response.receiverUserId()).isEqualTo(receiver.userID());
        assertThat(response.status()).isEqualTo(FriendRequestStatus.PENDING);
        verify(eventPublisher).publishFriendRequestSent(any(FriendRequestSentEvent.class));
    }

    @Test
    void rejectsDuplicatedPendingRequest() {
        UserSummary sender = fakeUser();
        UserSummary receiver = fakeUser();
        FriendRequest pendingRequest = FriendRequest.create(sender.userID(), receiver.userID());

        when(userDirectory.findById(sender.userID())).thenReturn(sender);
        when(userDirectory.findById(receiver.userID())).thenReturn(receiver);
        when(friendshipRepository.findFriendshipBetween(sender.userID(), receiver.userID())).thenReturn(Optional.empty());
        when(friendshipRepository.findPendingRequestBetween(sender.userID(), receiver.userID())).thenReturn(Optional.of(pendingRequest));

        assertThatThrownBy(() -> friendshipService.sendFriendRequest(new SendFriendRequest(
                sender.userID(),
                receiver.userID(),
                null,
                null
        )))
                .isInstanceOf(FriendshipAlreadyExistsException.class)
                .hasMessageContaining("solicitud pendiente");
    }

    @Test
    void acceptsRequestAndCreatesFriendship() {
        UserSummary sender = fakeUser();
        UserSummary receiver = fakeUser();
        FriendRequest request = FriendRequest.create(sender.userID(), receiver.userID());
        UUID conversationId = UUID.randomUUID();

        when(friendshipRepository.findRequestById(request.getId())).thenReturn(Optional.of(request));
        when(friendshipRepository.saveFriendship(any(Friendship.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(friendshipRepository.saveRequest(any(FriendRequest.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(conversationDirectory.createPrivateConversation(sender.userID(), receiver.userID())).thenReturn(conversationId);

        FriendshipResponse response = friendshipService.acceptRequest(
                request.getId(),
                new RespondFriendRequest(receiver.userID())
        );

        assertThat(response.status()).isEqualTo(FriendshipStatus.ACTIVE);
        assertThat(response.conversationId()).isEqualTo(conversationId);
        assertThat(request.getStatus()).isEqualTo(FriendRequestStatus.ACCEPTED);
        verify(conversationDirectory).createPrivateConversation(sender.userID(), receiver.userID());
        verify(friendshipRepository).saveRequest(request);
        verify(eventPublisher).publishFriendRequestAccepted(any(FriendRequestAcceptedEvent.class));
    }

    @Test
    void publishesRequestIdWhenSendingRequest() {
        UserSummary sender = fakeUser();
        UserSummary receiver = fakeUser();

        when(userDirectory.findById(sender.userID())).thenReturn(sender);
        when(userDirectory.findByFriendCode(receiver.friendCode())).thenReturn(receiver);
        when(friendshipRepository.findFriendshipBetween(sender.userID(), receiver.userID())).thenReturn(Optional.empty());
        when(friendshipRepository.findPendingRequestBetween(sender.userID(), receiver.userID())).thenReturn(Optional.empty());
        when(friendshipRepository.saveRequest(any(FriendRequest.class))).thenAnswer(invocation -> invocation.getArgument(0));

        FriendRequestResponse response = friendshipService.sendFriendRequest(new SendFriendRequest(
                sender.userID(),
                null,
                null,
                receiver.friendCode()
        ));

        ArgumentCaptor<FriendRequestSentEvent> eventCaptor = ArgumentCaptor.forClass(FriendRequestSentEvent.class);
        verify(eventPublisher).publishFriendRequestSent(eventCaptor.capture());

        assertThat(eventCaptor.getValue().requestId()).isEqualTo(response.id());
    }

    private UserSummary fakeUser() {
        String email = faker.internet().emailAddress();
        UUID userId = UUID.nameUUIDFromBytes(email.getBytes(StandardCharsets.UTF_8));

        return new UserSummary(
                userId,
                email.substring(0, email.indexOf("@")).toLowerCase(Locale.ROOT),
                faker.name().fullName(),
                email,
                faker.regexify("[A-F0-9]{12}")
        );
    }
}
