package cl.orioneta.conversations.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cl.orioneta.conversations.application.dto.CreateConversationRequestDTO;
import cl.orioneta.conversations.domain.model.Conversation;
import cl.orioneta.conversations.domain.model.ConversationType;
import cl.orioneta.conversations.domain.repository.ConversationRepositoryPort;
import java.util.List;
import java.util.UUID;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Pruebas del caso de uso que crea chats privados.
 */
@ExtendWith(MockitoExtension.class)
class CreatePrivateConversationUseCaseTest {

    private final Faker faker = new Faker();

    @Mock
    private ConversationRepositoryPort conversationRepositoryPort;

    private CreatePrivateConversationUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new CreatePrivateConversationUseCase(conversationRepositoryPort);
    }

    @Test
    void executeCreatesAndPersistsPrivateConversation() {
        UUID firstUserId = UUID.randomUUID();
        UUID secondUserId = UUID.randomUUID();
        CreateConversationRequestDTO request = new CreateConversationRequestDTO(
                ConversationType.PRIVATE_CHAT,
                faker.team().name(),
                null,
                null,
                List.of(firstUserId, secondUserId)
        );
        when(conversationRepositoryPort.save(any(Conversation.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Conversation conversation = useCase.execute(request);

        verify(conversationRepositoryPort).save(any(Conversation.class));
        assertThat(conversation.getType()).isEqualTo(ConversationType.PRIVATE_CHAT);
        assertThat(conversation.hasParticipant(firstUserId)).isTrue();
        assertThat(conversation.hasParticipant(secondUserId)).isTrue();
    }

    @Test
    void executeRejectsPrivateConversationWithInvalidParticipantCount() {
        CreateConversationRequestDTO request = new CreateConversationRequestDTO(
                ConversationType.PRIVATE_CHAT,
                null,
                null,
                null,
                List.of(UUID.randomUUID())
        );

        assertThatThrownBy(() -> useCase.execute(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Un chat privado necesita dos participantes");

        verify(conversationRepositoryPort, never()).save(any(Conversation.class));
    }
}
