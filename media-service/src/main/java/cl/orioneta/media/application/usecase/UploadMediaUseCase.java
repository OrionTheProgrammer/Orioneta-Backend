package cl.orioneta.media.application.usecase;

import cl.orioneta.media.application.dto.MediaUploadRequestDTO;
import cl.orioneta.media.domain.model.MediaFile;
import cl.orioneta.media.domain.repository.MediaRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class UploadMediaUseCase {

    private final MediaRepositoryPort mediaRepositoryPort;

    public UploadMediaUseCase(MediaRepositoryPort mediaRepositoryPort) {
        this.mediaRepositoryPort = mediaRepositoryPort;
    }

    public MediaFile execute(MediaUploadRequestDTO request) {
        return mediaRepositoryPort.save(MediaFile.create(
                request.ownerUserId(),
                request.fileName(),
                request.contentType(),
                request.size(),
                request.url(),
                request.purpose()
        ));
    }
}
