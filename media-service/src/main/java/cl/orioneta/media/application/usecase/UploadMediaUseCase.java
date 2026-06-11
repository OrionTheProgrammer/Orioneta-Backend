package cl.orioneta.media.application.usecase;

import cl.orioneta.media.application.dto.MediaUploadRequestDTO;
import cl.orioneta.media.application.dto.UploadMediaFileCommand;
import cl.orioneta.media.application.storage.MediaStoragePort;
import cl.orioneta.media.domain.model.MediaFile;
import cl.orioneta.media.domain.repository.MediaRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UploadMediaUseCase {

    private final MediaRepositoryPort mediaRepositoryPort;
    private final MediaStoragePort mediaStoragePort;

    public UploadMediaUseCase(MediaRepositoryPort mediaRepositoryPort, MediaStoragePort mediaStoragePort) {
        this.mediaRepositoryPort = mediaRepositoryPort;
        this.mediaStoragePort = mediaStoragePort;
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

    public MediaFile upload(UploadMediaFileCommand command) {
        UUID mediaId = UUID.randomUUID();
        String contentType = normalizeContentType(command.contentType());

        mediaStoragePort.store(
                mediaId,
                command.fileName(),
                contentType,
                command.size(),
                command.inputStream()
        );

        MediaFile mediaFile = MediaFile.createWithId(
                mediaId,
                command.ownerUserId(),
                command.fileName(),
                contentType,
                command.size(),
                mediaStoragePort.buildPublicUrl(mediaId),
                command.purpose()
        );

        return mediaRepositoryPort.save(mediaFile);
    }

    private String normalizeContentType(String contentType) {
        if (contentType == null || contentType.isBlank()) {
            return "application/octet-stream";
        }

        return contentType;
    }
}
