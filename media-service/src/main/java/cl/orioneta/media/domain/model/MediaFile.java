package cl.orioneta.media.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Metadatos de un archivo usado por Orioneta.
 */
public class MediaFile {

    private final UUID id;
    private final UUID ownerUserId;
    private String fileName;
    private String contentType;
    private long size;
    private String url;
    private MediaPurpose purpose;
    private final LocalDateTime createdAt;
    private LocalDateTime deletedAt;

    private MediaFile(
            UUID id,
            UUID ownerUserId,
            String fileName,
            String contentType,
            long size,
            String url,
            MediaPurpose purpose,
            LocalDateTime createdAt,
            LocalDateTime deletedAt
    ) {
        this.id = Objects.requireNonNull(id, "El id del archivo es obligatorio");
        this.ownerUserId = Objects.requireNonNull(ownerUserId, "El owner es obligatorio");
        this.fileName = requireText(fileName, "El nombre del archivo es obligatorio");
        this.contentType = requireText(contentType, "El content type es obligatorio");
        this.size = validateSize(size);
        this.url = requireText(url, "La URL del archivo es obligatoria");
        this.purpose = Objects.requireNonNull(purpose, "El proposito del archivo es obligatorio");
        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
        this.deletedAt = deletedAt;
    }

    public static MediaFile create(UUID ownerUserId, String fileName, String contentType, long size, String url, MediaPurpose purpose) {
        return new MediaFile(UUID.randomUUID(), ownerUserId, fileName, contentType, size, url, purpose, LocalDateTime.now(), null);
    }

    public static MediaFile createWithId(UUID id, UUID ownerUserId, String fileName, String contentType, long size, String url, MediaPurpose purpose) {
        return new MediaFile(id, ownerUserId, fileName, contentType, size, url, purpose, LocalDateTime.now(), null);
    }

    public static MediaFile rehydrate(
            UUID id,
            UUID ownerUserId,
            String fileName,
            String contentType,
            long size,
            String url,
            MediaPurpose purpose,
            LocalDateTime createdAt,
            LocalDateTime deletedAt
    ) {
        return new MediaFile(id, ownerUserId, fileName, contentType, size, url, purpose, createdAt, deletedAt);
    }

    public void delete() {
        deletedAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public UUID getOwnerUserId() {
        return ownerUserId;
    }

    public String getFileName() {
        return fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public long getSize() {
        return size;
    }

    public String getUrl() {
        return url;
    }

    public MediaPurpose getPurpose() {
        return purpose;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    private static long validateSize(long size) {
        if (size < 0) {
            throw new IllegalArgumentException("El tamano del archivo no puede ser negativo");
        }

        return size;
    }

    private static String requireText(String value, String message) {
        if (value == null || value.trim().isBlank()) {
            throw new IllegalArgumentException(message);
        }

        return value.trim();
    }
}
