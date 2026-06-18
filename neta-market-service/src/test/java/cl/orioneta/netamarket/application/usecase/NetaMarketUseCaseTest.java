package cl.orioneta.netamarket.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cl.orioneta.netamarket.application.dto.NetaTemplateRequestDTO;
import cl.orioneta.netamarket.domain.exception.NetaTemplateNotFoundException;
import cl.orioneta.netamarket.domain.model.NetaTemplate;
import cl.orioneta.netamarket.domain.model.NetaTemplateStatus;
import cl.orioneta.netamarket.domain.model.NetaTemplateType;
import cl.orioneta.netamarket.domain.repository.NetaTemplateRepositoryPort;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Pruebas de los casos de uso de Neta Market.
 */
@ExtendWith(MockitoExtension.class)
class NetaMarketUseCaseTest {

    private final Faker faker = new Faker();

    @Mock
    private NetaTemplateRepositoryPort repository;

    private PublishTemplateUseCase publishUseCase;
    private DownloadTemplateUseCase downloadUseCase;
    private SearchTemplatesUseCase searchUseCase;

    @BeforeEach
    void setUp() {
        publishUseCase = new PublishTemplateUseCase(repository);
        downloadUseCase = new DownloadTemplateUseCase(repository);
        searchUseCase = new SearchTemplatesUseCase(repository);
    }

    @Test
    void publishCreatesTemplateInPendingReview() {
        NetaTemplateRequestDTO request = new NetaTemplateRequestDTO(
                UUID.randomUUID(),
                faker.lorem().word(),
                faker.lorem().sentence(),
                NetaTemplateType.BACKGROUND,
                "https://media.orioneta.cl/preview.png",
                "https://media.orioneta.cl/file.zip",
                "1.2.0"
        );
        when(repository.save(any(NetaTemplate.class))).thenAnswer(invocation -> invocation.getArgument(0));

        NetaTemplate template = publishUseCase.execute(request);

        verify(repository).save(any(NetaTemplate.class));
        assertThat(template.getAuthorUserId()).isEqualTo(request.authorUserId());
        assertThat(template.getStatus()).isEqualTo(NetaTemplateStatus.PENDING_REVIEW);
        assertThat(template.getVersion()).isEqualTo("1.2.0");
    }

    @Test
    void downloadIncrementsTemplateCounter() {
        NetaTemplate template = NetaTemplate.publish(
                UUID.randomUUID(),
                faker.lorem().word(),
                faker.lorem().sentence(),
                NetaTemplateType.CHAT_THEME,
                "",
                "https://media.orioneta.cl/theme.zip",
                "1.0.0"
        );
        when(repository.findById(template.getId())).thenReturn(Optional.of(template));
        when(repository.save(any(NetaTemplate.class))).thenAnswer(invocation -> invocation.getArgument(0));

        NetaTemplate updated = downloadUseCase.execute(template.getId());

        assertThat(updated.getDownloads()).isEqualTo(1);
    }

    @Test
    void downloadFailsWhenTemplateDoesNotExist() {
        UUID templateId = UUID.randomUUID();
        when(repository.findById(templateId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> downloadUseCase.execute(templateId))
                .isInstanceOf(NetaTemplateNotFoundException.class)
                .hasMessage("Template no encontrado");
    }

    @Test
    void searchDelegatesFiltersToRepository() {
        List<NetaTemplate> templates = List.of(NetaTemplate.publish(
                UUID.randomUUID(),
                "aurora",
                "tema",
                NetaTemplateType.GLOBAL_THEME,
                "",
                "https://media.orioneta.cl/aurora.zip",
                "1.0.0"
        ));
        when(repository.search("aurora", NetaTemplateType.GLOBAL_THEME, NetaTemplateStatus.APPROVED))
                .thenReturn(templates);

        assertThat(searchUseCase.execute("aurora", NetaTemplateType.GLOBAL_THEME, NetaTemplateStatus.APPROVED))
                .containsExactlyElementsOf(templates);
    }
}
