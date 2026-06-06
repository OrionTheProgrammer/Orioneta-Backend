package cl.orioneta.media.application.usecase;

import cl.orioneta.media.application.storage.MediaStoragePort;
import cl.orioneta.media.application.storage.StoredMediaContent;
import cl.orioneta.media.domain.exception.MediaNotFoundException;
import cl.orioneta.media.domain.model.MediaFile;
import cl.orioneta.media.domain.repository.MediaRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DownloadMediaUseCase {

    private final MediaRepositoryPort mediaRepositoryPort;
    private final MediaStoragePort mediaStoragePort;

    public DownloadMediaUseCase(MediaRepositoryPort mediaRepositoryPort, MediaStoragePort mediaStoragePort) {
        this.mediaRepositoryPort = mediaRepositoryPort;
        this.mediaStoragePort = mediaStoragePort;
    }

    public StoredMediaContent execute(UUID mediaId) {
        MediaFile mediaFile = mediaRepositoryPort.findById(mediaId)
                .orElseThrow(() -> new MediaNotFoundException("Archivo no encontrado"));

        if (mediaFile.getDeletedAt() != null) {
            throw new MediaNotFoundException("Archivo no encontrado");
        }

        if (mediaFile.getStorageKey() == null || mediaFile.getStorageKey().isBlank()) {
            throw new IllegalArgumentException("El archivo no fue almacenado en MinIO");
        }

        return mediaStoragePort.load(mediaFile.getStorageKey());
    }
}
