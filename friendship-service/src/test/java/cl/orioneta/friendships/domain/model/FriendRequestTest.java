package cl.orioneta.friendships.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;

class FriendRequestTest {

    private final Faker faker = new Faker();

    @Test
    void createsPendingRequestBetweenDifferentUsers() {
        UUID senderUserId = fakeUserId();
        UUID receiverUserId = fakeUserId();

        FriendRequest request = FriendRequest.create(senderUserId, receiverUserId);

        assertThat(request.getId()).isNotNull();
        assertThat(request.getSenderUserId()).isEqualTo(senderUserId);
        assertThat(request.getReceiverUserId()).isEqualTo(receiverUserId);
        assertThat(request.getStatus()).isEqualTo(FriendRequestStatus.PENDING);
        assertThat(request.getCreatedAt()).isNotNull();
        assertThat(request.getRespondedAt()).isNull();
    }

    @Test
    void acceptsOnlyByReceiver() {
        UUID senderUserId = fakeUserId();
        UUID receiverUserId = fakeUserId();
        FriendRequest request = FriendRequest.create(senderUserId, receiverUserId);

        request.accept(receiverUserId);

        assertThat(request.getStatus()).isEqualTo(FriendRequestStatus.ACCEPTED);
        assertThat(request.getRespondedAt()).isNotNull();
    }

    @Test
    void rejectsSelfRequest() {
        UUID userId = fakeUserId();

        assertThatThrownBy(() -> FriendRequest.create(userId, userId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ti mismo");
    }

    private UUID fakeUserId() {
        return UUID.nameUUIDFromBytes(faker.internet().emailAddress().getBytes(StandardCharsets.UTF_8));
    }
}
