package cl.orioneta.media.application.usecase;

import cl.orioneta.media.domain.exception.MediaNotFoundException;
import cl.orioneta.media.domain.model.MediaFile;
import cl.orioneta.media.domain.repository.MediaRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FindMediaUseCase {

    private final MediaRepositoryPort mediaRepositoryPort;

    public FindMediaUseCase(MediaRepositoryPort mediaRepositoryPort) {
        this.mediaRepositoryPort = mediaRepositoryPort;
    }

    public MediaFile findById(UUID id) {
        return mediaRepositoryPort.findById(id)
                .orElseThrow(() -> new MediaNotFoundException("Archivo multimedia no encontrado"));
    }

    public List<MediaFile> findByOwner(UUID ownerUserId) {
        return mediaRepositoryPort.findByOwnerUserId(ownerUserId);
    }
}
