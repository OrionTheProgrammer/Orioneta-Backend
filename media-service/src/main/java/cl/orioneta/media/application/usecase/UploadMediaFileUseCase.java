package cl.orioneta.media.application.usecase;

import cl.orioneta.media.application.storage.MediaStoragePort;
import cl.orioneta.media.application.storage.StoreMediaFileCommand;
import cl.orioneta.media.application.storage.StoredMediaObject;
import cl.orioneta.media.domain.model.MediaFile;
import cl.orioneta.media.domain.repository.MediaRepositoryPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UploadMediaFileUseCase {

    private static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";

    private final MediaRepositoryPort mediaRepositoryPort;
    private final MediaStoragePort mediaStoragePort;
    private final String publicContentBaseUrl;

    public UploadMediaFileUseCase(
            MediaRepositoryPort mediaRepositoryPort,
            MediaStoragePort mediaStoragePort,
            @Value("${orioneta.media.public-content-base-url:/api/media}") String publicContentBaseUrl
    ) {
        this.mediaRepositoryPort = mediaRepositoryPort;
        this.mediaStoragePort = mediaStoragePort;
        this.publicContentBaseUrl = publicContentBaseUrl;
    }

    public MediaFile execute(StoreMediaFileCommand command) {
        validate(command);

        StoredMediaObject storedObject = mediaStoragePort.store(command);
        String url = buildContentUrl(command.mediaId());

        MediaFile mediaFile = MediaFile.createStored(
                command.mediaId(),
                command.ownerUserId(),
                cleanFileName(command.fileName()),
                cleanContentType(storedObject.contentType()),
                storedObject.size(),
                url,
                storedObject.storageKey(),
                command.purpose()
        );

        return mediaRepositoryPort.save(mediaFile);
    }

    private void validate(StoreMediaFileCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("Los datos del archivo son obligatorios");
        }

        if (command.mediaId() == null) {
            throw new IllegalArgumentException("El id del archivo es obligatorio");
        }

        if (command.ownerUserId() == null) {
            throw new IllegalArgumentException("El owner es obligatorio");
        }

        if (command.purpose() == null) {
            throw new IllegalArgumentException("El proposito es obligatorio");
        }

        if (command.content() == null) {
            throw new IllegalArgumentException("El contenido del archivo es obligatorio");
        }

        if (command.size() <= 0) {
            throw new IllegalArgumentException("El archivo no puede estar vacio");
        }
    }

    private String buildContentUrl(UUID mediaId) {
        String baseUrl = publicContentBaseUrl == null || publicContentBaseUrl.isBlank()
                ? "/api/media"
                : publicContentBaseUrl.trim();

        return removeTrailingSlash(baseUrl) + "/" + mediaId + "/content";
    }

    private String cleanFileName(String fileName) {
        if (fileName == null || fileName.trim().isBlank()) {
            return "archivo";
        }

        return fileName.trim();
    }

    private String cleanContentType(String contentType) {
        if (contentType == null || contentType.trim().isBlank()) {
            return DEFAULT_CONTENT_TYPE;
        }

        return contentType.trim();
    }

    private String removeTrailingSlash(String value) {
        if (value.endsWith("/")) {
            return value.substring(0, value.length() - 1);
        }

        return value;
    }
}
