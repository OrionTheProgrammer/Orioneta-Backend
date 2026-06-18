package cl.orioneta.audit.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cl.orioneta.audit.application.dto.AuditEventRequestDTO;
import cl.orioneta.audit.domain.model.AuditEvent;
import cl.orioneta.audit.domain.repository.AuditRepositoryPort;
import java.util.List;
import java.util.UUID;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Pruebas de casos de uso de auditoria.
 */
@ExtendWith(MockitoExtension.class)
class AuditUseCaseTest {

    private final Faker faker = new Faker();

    @Mock
    private AuditRepositoryPort auditRepositoryPort;

    private RegisterAuditEventUseCase registerUseCase;
    private FindAuditEventsUseCase findUseCase;

    @BeforeEach
    void setUp() {
        registerUseCase = new RegisterAuditEventUseCase(auditRepositoryPort);
        findUseCase = new FindAuditEventsUseCase(auditRepositoryPort);
    }

    @Test
    void registerPersistsAuditEvent() {
        AuditEventRequestDTO request = new AuditEventRequestDTO(
                "message-service",
                "MESSAGE_SENT",
                "MESSAGE",
                UUID.randomUUID(),
                UUID.randomUUID(),
                faker.lorem().sentence()
        );
        when(auditRepositoryPort.save(any(AuditEvent.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AuditEvent event = registerUseCase.execute(request);

        verify(auditRepositoryPort).save(any(AuditEvent.class));
        assertThat(event.getSourceService()).isEqualTo(request.sourceService());
        assertThat(event.getAction()).isEqualTo(request.action());
        assertThat(event.getTargetId()).isEqualTo(request.targetId());
    }

    @Test
    void findByTargetDelegatesToRepository() {
        UUID targetId = UUID.randomUUID();
        List<AuditEvent> events = List.of(AuditEvent.create(
                "auth-service",
                "LOGIN",
                "USER",
                targetId,
                UUID.randomUUID(),
                "ok"
        ));
        when(auditRepositoryPort.findByTarget("USER", targetId)).thenReturn(events);

        assertThat(findUseCase.findByTarget("USER", targetId)).containsExactlyElementsOf(events);
    }

    @Test
    void findRecentDelegatesToRepository() {
        List<AuditEvent> events = List.of(AuditEvent.create(
                "auth-service",
                "LOGIN",
                "USER",
                UUID.randomUUID(),
                UUID.randomUUID(),
                "ok"
        ));
        when(auditRepositoryPort.findRecent()).thenReturn(events);

        assertThat(findUseCase.findRecent()).containsExactlyElementsOf(events);
    }
}
