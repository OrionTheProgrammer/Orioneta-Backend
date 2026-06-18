package cl.orioneta.media.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import cl.orioneta.media.domain.exception.MediaNotFoundException;
import cl.orioneta.media.domain.model.MediaFile;
import cl.orioneta.media.domain.model.MediaPurpose;
import cl.orioneta.media.domain.repository.MediaRepositoryPort;
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
 * Pruebas de busqueda de archivos multimedia.
 */
@ExtendWith(MockitoExtension.class)
class FindMediaUseCaseTest {

    private final Faker faker = new Faker();

    @Mock
    private MediaRepositoryPort mediaRepositoryPort;

    private FindMediaUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new FindMediaUseCase(mediaRepositoryPort);
    }

    @Test
    void findByIdReturnsMediaWhenItExists() {
        MediaFile mediaFile = mediaFile(UUID.randomUUID());
        when(mediaRepositoryPort.findById(mediaFile.getId())).thenReturn(Optional.of(mediaFile));

        MediaFile result = useCase.findById(mediaFile.getId());

        assertThat(result).isEqualTo(mediaFile);
    }

    @Test
    void findByIdThrowsWhenMediaDoesNotExist() {
        UUID mediaId = UUID.randomUUID();
        when(mediaRepositoryPort.findById(mediaId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.findById(mediaId))
                .isInstanceOf(MediaNotFoundException.class)
                .hasMessage("Archivo multimedia no encontrado");
    }

    @Test
    void findByOwnerReturnsOwnerMedia() {
        UUID ownerUserId = UUID.randomUUID();
        List<MediaFile> files = List.of(mediaFile(ownerUserId), mediaFile(ownerUserId));
        when(mediaRepositoryPort.findByOwnerUserId(ownerUserId)).thenReturn(files);

        List<MediaFile> result = useCase.findByOwner(ownerUserId);

        assertThat(result).containsExactlyElementsOf(files);
    }

    private MediaFile mediaFile(UUID ownerUserId) {
        return MediaFile.create(
                ownerUserId,
                faker.file().fileName(),
                "image/png",
                128,
                "https://media.orioneta.cl/" + UUID.randomUUID(),
                MediaPurpose.AVATAR
        );
    }
}
