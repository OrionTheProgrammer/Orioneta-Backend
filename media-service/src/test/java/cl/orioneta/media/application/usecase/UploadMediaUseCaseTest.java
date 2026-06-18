package cl.orioneta.media.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cl.orioneta.media.application.dto.MediaUploadRequestDTO;
import cl.orioneta.media.application.dto.UploadMediaFileCommand;
import cl.orioneta.media.application.storage.MediaStoragePort;
import cl.orioneta.media.domain.model.MediaFile;
import cl.orioneta.media.domain.model.MediaPurpose;
import cl.orioneta.media.domain.repository.MediaRepositoryPort;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Pruebas del flujo de registro y subida de archivos multimedia.
 */
@ExtendWith(MockitoExtension.class)
class UploadMediaUseCaseTest {

    private final Faker faker = new Faker();

    @Mock
    private MediaRepositoryPort mediaRepositoryPort;

    @Mock
    private MediaStoragePort mediaStoragePort;

    private UploadMediaUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new UploadMediaUseCase(mediaRepositoryPort, mediaStoragePort);
    }

    @Test
    void executeRegistersAlreadyUploadedMediaWithoutCallingStorage() {
        MediaUploadRequestDTO request = new MediaUploadRequestDTO(
                UUID.randomUUID(),
                "avatar.png",
                "image/png",
                128,
                "https://cdn.orioneta.cl/avatar.png",
                MediaPurpose.AVATAR
        );
        when(mediaRepositoryPort.save(any(MediaFile.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MediaFile mediaFile = useCase.execute(request);

        verify(mediaRepositoryPort).save(any(MediaFile.class));
        assertThat(mediaFile.getOwnerUserId()).isEqualTo(request.ownerUserId());
        assertThat(mediaFile.getUrl()).isEqualTo(request.url());
        assertThat(mediaFile.getPurpose()).isEqualTo(MediaPurpose.AVATAR);
    }

    @Test
    void uploadStoresBinaryAndSavesMetadataWithPublicUrl() {
        UUID ownerUserId = UUID.randomUUID();
        byte[] content = faker.lorem().sentence().getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(content);
        UploadMediaFileCommand command = new UploadMediaFileCommand(
                ownerUserId,
                "mensaje.txt",
                "text/plain",
                content.length,
                MediaPurpose.MESSAGE_ATTACHMENT,
                inputStream
        );
        when(mediaStoragePort.buildPublicUrl(any(UUID.class)))
                .thenAnswer(invocation -> "https://media.orioneta.cl/" + invocation.getArgument(0));
        when(mediaRepositoryPort.save(any(MediaFile.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MediaFile mediaFile = useCase.upload(command);

        ArgumentCaptor<UUID> mediaIdCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(mediaStoragePort).store(
                mediaIdCaptor.capture(),
                eq(command.fileName()),
                eq(command.contentType()),
                eq(command.size()),
                eq(command.inputStream())
        );
        verify(mediaRepositoryPort).save(any(MediaFile.class));

        assertThat(mediaFile.getId()).isEqualTo(mediaIdCaptor.getValue());
        assertThat(mediaFile.getOwnerUserId()).isEqualTo(ownerUserId);
        assertThat(mediaFile.getUrl()).isEqualTo("https://media.orioneta.cl/" + mediaFile.getId());
        assertThat(mediaFile.getPurpose()).isEqualTo(MediaPurpose.MESSAGE_ATTACHMENT);
    }

    @Test
    void uploadUsesDefaultContentTypeWhenCommandDoesNotProvideOne() {
        byte[] content = "orioneta".getBytes(StandardCharsets.UTF_8);
        UploadMediaFileCommand command = new UploadMediaFileCommand(
                UUID.randomUUID(),
                "archivo.bin",
                " ",
                content.length,
                MediaPurpose.MESSAGE_ATTACHMENT,
                new ByteArrayInputStream(content)
        );
        when(mediaStoragePort.buildPublicUrl(any(UUID.class)))
                .thenAnswer(invocation -> "https://media.orioneta.cl/" + invocation.getArgument(0));
        when(mediaRepositoryPort.save(any(MediaFile.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MediaFile mediaFile = useCase.upload(command);

        assertThat(mediaFile.getContentType()).isEqualTo("application/octet-stream");
        verify(mediaStoragePort).store(
                any(UUID.class),
                eq(command.fileName()),
                eq("application/octet-stream"),
                eq(command.size()),
                eq(command.inputStream())
        );
    }
}
