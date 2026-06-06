package cl.orioneta.media.infrastructure.out.storage;

import cl.orioneta.media.application.storage.MediaStoragePort;
import cl.orioneta.media.application.storage.StoreMediaFileCommand;
import cl.orioneta.media.application.storage.StoredMediaContent;
import cl.orioneta.media.application.storage.StoredMediaObject;
import cl.orioneta.media.domain.exception.MediaStorageException;
import cl.orioneta.media.infrastructure.config.MinioProperties;
import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.stereotype.Component;

import java.text.Normalizer;
import java.time.LocalDate;
import java.util.Locale;

@Component
public class MinioMediaStorageAdapter implements MediaStoragePort {

    private static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";

    private final MinioClient minioClient;
    private final MinioProperties properties;

    public MinioMediaStorageAdapter(MinioClient minioClient, MinioProperties properties) {
        this.minioClient = minioClient;
        this.properties = properties;
    }

    @Override
    public StoredMediaObject store(StoreMediaFileCommand command) {
        ensureBucketExists();

        String objectName = buildObjectName(command);
        String contentType = cleanContentType(command.contentType());

        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(properties.getBucket())
                    .object(objectName)
                    .stream(command.content(), command.size(), -1)
                    .contentType(contentType)
                    .build());

            return new StoredMediaObject(objectName, contentType, command.size());
        } catch (Exception exception) {
            throw new MediaStorageException("No se pudo guardar el archivo en MinIO", exception);
        }
    }

    @Override
    public StoredMediaContent load(String storageKey) {
        try {
            GetObjectResponse response = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(properties.getBucket())
                    .object(storageKey)
                    .build());

            String contentType = response.headers().get("Content-Type");
            String contentLength = response.headers().get("Content-Length");
            long size = contentLength == null ? -1 : Long.parseLong(contentLength);

            return new StoredMediaContent(response, cleanContentType(contentType), size);
        } catch (Exception exception) {
            throw new MediaStorageException("No se pudo recuperar el archivo desde MinIO", exception);
        }
    }

    private void ensureBucketExists() {
        try {
            boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(properties.getBucket())
                    .build());

            if (!bucketExists) {
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(properties.getBucket())
                        .build());
            }
        } catch (Exception exception) {
            throw new MediaStorageException("No se pudo preparar el bucket de MinIO", exception);
        }
    }

    private String buildObjectName(StoreMediaFileCommand command) {
        LocalDate today = LocalDate.now();

        return "%s/%s/%04d/%02d/%02d/%s/%s".formatted(
                command.ownerUserId(),
                command.purpose().name().toLowerCase(Locale.ROOT),
                today.getYear(),
                today.getMonthValue(),
                today.getDayOfMonth(),
                command.mediaId(),
                sanitizeFileName(command.fileName())
        );
    }

    private String sanitizeFileName(String fileName) {
        String safeName = fileName == null || fileName.trim().isBlank()
                ? "archivo"
                : fileName.trim();

        String normalized = Normalizer.normalize(safeName, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");

        String sanitized = normalized
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9._-]+", "-")
                .replaceAll("^-+|-+$", "");

        return sanitized.isBlank() ? "archivo" : sanitized;
    }

    private String cleanContentType(String contentType) {
        if (contentType == null || contentType.trim().isBlank()) {
            return DEFAULT_CONTENT_TYPE;
        }

        return contentType.trim();
    }
}
