package cl.orioneta.media.infrastructure.out.persistence;

import cl.orioneta.media.domain.model.MediaPurpose;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "media_files")
public class MediaEntity {

    @Id
    private UUID id;

    @Column(name = "owner_user_id", nullable = false)
    private UUID ownerUserId;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "content_type", nullable = false)
    private String contentType;

    @Column(nullable = false)
    private long size;

    @Column(nullable = false, length = 1000)
    private String url;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private MediaPurpose purpose;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    protected MediaEntity() {
    }

    public MediaEntity(UUID id, UUID ownerUserId, String fileName, String contentType, long size, String url, MediaPurpose purpose, LocalDateTime createdAt, LocalDateTime deletedAt) {
        this.id = id;
        this.ownerUserId = ownerUserId;
        this.fileName = fileName;
        this.contentType = contentType;
        this.size = size;
        this.url = url;
        this.purpose = purpose;
        this.createdAt = createdAt;
        this.deletedAt = deletedAt;
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
}
