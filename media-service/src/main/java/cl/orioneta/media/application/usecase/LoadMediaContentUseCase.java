package cl.orioneta.media.application.usecase;

import cl.orioneta.media.application.storage.MediaContent;
import cl.orioneta.media.application.storage.MediaStoragePort;
import cl.orioneta.media.domain.model.MediaFile;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LoadMediaContentUseCase {

    private final FindMediaUseCase findMediaUseCase;
    private final MediaStoragePort mediaStoragePort;

    public LoadMediaContentUseCase(FindMediaUseCase findMediaUseCase, MediaStoragePort mediaStoragePort) {
        this.findMediaUseCase = findMediaUseCase;
        this.mediaStoragePort = mediaStoragePort;
    }

    public MediaContent execute(UUID id) {
        MediaFile mediaFile = findMediaUseCase.findById(id);
        return mediaStoragePort.load(mediaFile);
    }
}
