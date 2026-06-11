package cl.orioneta.media.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "orioneta.media.storage")
public record MediaStorageProperties(
        String endpoint,
        String accessKey,
        String secretKey,
        String bucket,
        String publicBaseUrl
) {
    public MediaStorageProperties {
        if (endpoint == null || endpoint.isBlank()) {
            throw new IllegalArgumentException("orioneta.media.storage.endpoint es obligatorio");
        }

        if (accessKey == null || accessKey.isBlank()) {
            throw new IllegalArgumentException("orioneta.media.storage.access-key es obligatorio");
        }

        if (secretKey == null || secretKey.isBlank()) {
            throw new IllegalArgumentException("orioneta.media.storage.secret-key es obligatorio");
        }

        if (bucket == null || bucket.isBlank()) {
            throw new IllegalArgumentException("orioneta.media.storage.bucket es obligatorio");
        }

        if (publicBaseUrl == null || publicBaseUrl.isBlank()) {
            throw new IllegalArgumentException("orioneta.media.storage.public-base-url es obligatorio");
        }
    }
}
