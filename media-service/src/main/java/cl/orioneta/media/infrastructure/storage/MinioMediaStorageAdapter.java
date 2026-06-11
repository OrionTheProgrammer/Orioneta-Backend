package cl.orioneta.media.infrastructure.storage;

import cl.orioneta.media.application.storage.MediaContent;
import cl.orioneta.media.application.storage.MediaStoragePort;
import cl.orioneta.media.domain.model.MediaFile;
import cl.orioneta.media.infrastructure.config.MediaStorageProperties;
import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.UUID;

@Component
public class MinioMediaStorageAdapter implements MediaStoragePort {

    private final MinioClient minioClient;
    private final MediaStorageProperties properties;

    public MinioMediaStorageAdapter(MinioClient minioClient, MediaStorageProperties properties) {
        this.minioClient = minioClient;
        this.properties = properties;
        ensureBucket();
    }

    @Override
    public String buildPublicUrl(UUID mediaId) {
        return properties.publicBaseUrl().replaceAll("/+$", "") + "/api/media/" + mediaId + "/content";
    }

    @Override
    public void store(UUID mediaId, String fileName, String contentType, long size, InputStream inputStream) {
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(properties.bucket())
                    .object(objectName(mediaId))
                    .contentType(contentType)
                    .stream(inputStream, size, -1)
                    .build());
        } catch (Exception exception) {
            throw new IllegalStateException("No se pudo guardar el archivo en MinIO", exception);
        }
    }

    @Override
    public MediaContent load(MediaFile mediaFile) {
        try {
            InputStream objectStream = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(properties.bucket())
                    .object(objectName(mediaFile.getId()))
                    .build());

            return new MediaContent(objectStream, mediaFile.getContentType(), mediaFile.getSize());
        } catch (Exception exception) {
            throw new IllegalStateException("No se pudo leer el archivo desde MinIO", exception);
        }
    }

    private void ensureBucket() {
        try {
            boolean exists = minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(properties.bucket())
                    .build());

            if (!exists) {
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(properties.bucket())
                        .build());
            }
        } catch (Exception exception) {
            throw new IllegalStateException("No se pudo preparar el bucket de MinIO", exception);
        }
    }

    private String objectName(UUID mediaId) {
        return mediaId.toString();
    }
}
