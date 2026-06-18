package cl.orioneta.messages.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cl.orioneta.messages.application.conversation.ConversationLookupPort;
import cl.orioneta.messages.application.conversation.ConversationParticipantSummary;
import cl.orioneta.messages.application.conversation.ConversationSummary;
import cl.orioneta.messages.application.dto.SendMessageRequestDTO;
import cl.orioneta.messages.application.event.MessageEventPublisher;
import cl.orioneta.messages.domain.model.Message;
import cl.orioneta.messages.domain.model.MessageType;
import cl.orioneta.messages.domain.repository.MessageRepositoryPort;
import java.util.List;
import java.util.UUID;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Pruebas del caso de uso que envia mensajes.
 */
@ExtendWith(MockitoExtension.class)
class SendMessageUseCaseTest {

    private final Faker faker = new Faker();

    @Mock
    private MessageRepositoryPort messageRepositoryPort;

    @Mock
    private ConversationLookupPort conversationLookupPort;

    @Mock
    private MessageEventPublisher messageEventPublisher;

    private SendMessageUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new SendMessageUseCase(messageRepositoryPort, conversationLookupPort, messageEventPublisher);
    }

    @Test
    void executeSavesMessageAndPublishesEventWhenSenderBelongsToConversation() {
        UUID conversationId = UUID.randomUUID();
        UUID senderId = UUID.randomUUID();
        UUID receiverId = UUID.randomUUID();
        ConversationSummary conversation = new ConversationSummary(
                conversationId,
                List.of(new ConversationParticipantSummary(senderId), new ConversationParticipantSummary(receiverId))
        );
        SendMessageRequestDTO request = new SendMessageRequestDTO(
                conversationId,
                senderId,
                faker.lorem().sentence(),
                MessageType.TEXT
        );
        when(conversationLookupPort.findById(conversationId)).thenReturn(conversation);
        when(messageRepositoryPort.save(any(Message.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Message message = useCase.execute(request);

        ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
        verify(messageRepositoryPort).save(messageCaptor.capture());
        verify(messageEventPublisher).publishMessageSent(eq(message), eq(List.of(senderId, receiverId)));

        assertThat(messageCaptor.getValue().getConversationId()).isEqualTo(conversationId);
        assertThat(messageCaptor.getValue().getSenderId()).isEqualTo(senderId);
        assertThat(message.getContent()).isEqualTo(request.content());
    }

    @Test
    void executeRejectsMessageWhenSenderIsNotParticipant() {
        UUID conversationId = UUID.randomUUID();
        UUID senderId = UUID.randomUUID();
        ConversationSummary conversation = new ConversationSummary(
                conversationId,
                List.of(new ConversationParticipantSummary(UUID.randomUUID()))
        );
        SendMessageRequestDTO request = new SendMessageRequestDTO(
                conversationId,
                senderId,
                faker.lorem().sentence(),
                MessageType.TEXT
        );
        when(conversationLookupPort.findById(conversationId)).thenReturn(conversation);

        assertThatThrownBy(() -> useCase.execute(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("El emisor no participa en la conversacion");

        verify(messageRepositoryPort, never()).save(any(Message.class));
        verify(messageEventPublisher, never()).publishMessageSent(any(Message.class), any());
    }
}
