package cl.orioneta.friendships.domain.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;

class FriendshipTest {

    private final Faker faker = new Faker();

    @Test
    void createsActiveFriendshipWithOrderedUsers() {
        UUID firstUserId = fakeUserId();
        UUID secondUserId = fakeUserId();

        Friendship friendship = Friendship.create(firstUserId, secondUserId);

        assertThat(friendship.getId()).isNotNull();
        assertThat(friendship.getStatus()).isEqualTo(FriendshipStatus.ACTIVE);
        assertThat(friendship.containsUser(firstUserId)).isTrue();
        assertThat(friendship.containsUser(secondUserId)).isTrue();
        assertThat(friendship.getUserId().compareTo(friendship.getFriendId())).isLessThanOrEqualTo(0);
    }

    @Test
    void blockChangesStatus() {
        Friendship friendship = Friendship.create(fakeUserId(), fakeUserId());

        friendship.block();

        assertThat(friendship.getStatus()).isEqualTo(FriendshipStatus.BLOCKED);
    }

    private UUID fakeUserId() {
        return UUID.nameUUIDFromBytes(faker.internet().emailAddress().getBytes(StandardCharsets.UTF_8));
    }
}
