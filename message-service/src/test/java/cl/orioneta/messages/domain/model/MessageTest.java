package cl.orioneta.messages.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.UUID;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;

/**
 * Pruebas de reglas puras del mensaje.
 */
class MessageTest {

    private final Faker faker = new Faker();

    @Test
    void createNormalizesContentAndDefaultsTypeAndStatus() {
        UUID conversationId = UUID.randomUUID();
        UUID senderId = UUID.randomUUID();
        String content = "  " + faker.lorem().sentence() + "  ";

        Message message = Message.create(conversationId, senderId, content, null);

        assertThat(message.getConversationId()).isEqualTo(conversationId);
        assertThat(message.getSenderId()).isEqualTo(senderId);
        assertThat(message.getContent()).isEqualTo(content.trim());
        assertThat(message.getType()).isEqualTo(MessageType.TEXT);
        assertThat(message.getStatus()).isEqualTo(MessageStatus.SENT);
        assertThat(message.getDeletedAt()).isNull();
    }

    @Test
    void deleteMarksMessageAsDeletedAndPreventsEditing() {
        Message message = Message.create(
                UUID.randomUUID(),
                UUID.randomUUID(),
                faker.lorem().sentence(),
                MessageType.TEXT
        );

        message.delete();

        assertThat(message.getStatus()).isEqualTo(MessageStatus.DELETED);
        assertThat(message.getDeletedAt()).isNotNull();
        assertThatThrownBy(() -> message.edit("nuevo texto"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("No se puede editar un mensaje eliminado");
    }

    @Test
    void createRejectsBlankContent() {
        assertThatThrownBy(() -> Message.create(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "   ",
                MessageType.TEXT
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("El contenido del mensaje es obligatorio");
    }
}
