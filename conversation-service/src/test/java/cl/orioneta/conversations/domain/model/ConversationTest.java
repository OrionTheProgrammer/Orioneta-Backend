package cl.orioneta.conversations.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

/**
 * Pruebas de reglas puras de las conversaciones.
 */
class ConversationTest {

    @Test
    void createPrivateBuildsChatWithTwoDifferentMembers() {
        UUID firstUserId = UUID.randomUUID();
        UUID secondUserId = UUID.randomUUID();

        Conversation conversation = Conversation.createPrivate(firstUserId, secondUserId);

        assertThat(conversation.getType()).isEqualTo(ConversationType.PRIVATE_CHAT);
        assertThat(conversation.getOwnerId()).isNull();
        assertThat(conversation.getParticipants()).hasSize(2);
        assertThat(conversation.hasParticipant(firstUserId)).isTrue();
        assertThat(conversation.hasParticipant(secondUserId)).isTrue();
    }

    @Test
    void createPrivateRejectsSameUserTwice() {
        UUID userId = UUID.randomUUID();

        assertThatThrownBy(() -> Conversation.createPrivate(userId, userId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Un chat privado necesita dos usuarios distintos");
    }

    @Test
    void createGroupKeepsOwnerOnlyAsOwnerEvenIfItComesInMembers() {
        UUID ownerId = UUID.randomUUID();
        UUID memberId = UUID.randomUUID();

        Conversation conversation = Conversation.createGroup(
                ownerId,
                "Grupo Orioneta",
                "Grupo de pruebas",
                List.of(ownerId, memberId)
        );

        assertThat(conversation.getType()).isEqualTo(ConversationType.GROUP_CHAT);
        assertThat(conversation.getOwnerId()).isEqualTo(ownerId);
        assertThat(conversation.getName()).isEqualTo("Grupo Orioneta");
        assertThat(conversation.getParticipants()).hasSize(2);
        assertThat(conversation.getParticipants())
                .filteredOn(participant -> participant.getUserId().equals(ownerId))
                .singleElement()
                .extracting(Participant::getRole)
                .isEqualTo(ParticipantRole.OWNER);
        assertThat(conversation.hasParticipant(memberId)).isTrue();
    }
}
