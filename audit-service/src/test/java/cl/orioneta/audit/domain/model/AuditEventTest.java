package cl.orioneta.audit.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.UUID;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;

/**
 * Pruebas de reglas puras de auditoria.
 */
class AuditEventTest {

    private final Faker faker = new Faker();

    @Test
    void createNormalizesRequiredTextsAndOptionalDetail() {
        UUID targetId = UUID.randomUUID();
        UUID actorUserId = UUID.randomUUID();

        AuditEvent event = AuditEvent.create(
                " message-service ",
                " MESSAGE_SENT ",
                " MESSAGE ",
                targetId,
                actorUserId,
                "  " + faker.lorem().sentence() + "  "
        );

        assertThat(event.getSourceService()).isEqualTo("message-service");
        assertThat(event.getAction()).isEqualTo("MESSAGE_SENT");
        assertThat(event.getTargetType()).isEqualTo("MESSAGE");
        assertThat(event.getTargetId()).isEqualTo(targetId);
        assertThat(event.getActorUserId()).isEqualTo(actorUserId);
        assertThat(event.getDetail()).isEqualTo(event.getDetail().trim());
    }

    @Test
    void createRejectsBlankAction() {
        assertThatThrownBy(() -> AuditEvent.create(
                "message-service",
                " ",
                "MESSAGE",
                UUID.randomUUID(),
                UUID.randomUUID(),
                "detalle"
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("La accion es obligatoria");
    }
}
