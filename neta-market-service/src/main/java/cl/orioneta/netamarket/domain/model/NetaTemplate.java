package cl.orioneta.netamarket.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Template visual publicado en Neta Market.
 */
public class NetaTemplate {

    private final UUID id;
    private final UUID authorUserId;
    private String name;
    private String description;
    private NetaTemplateType type;
    private NetaTemplateStatus status;
    private String previewImageUrl;
    private String fileUrl;
    private String version;
    private long downloads;
    private double ratingAverage;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private NetaTemplate(UUID id, UUID authorUserId, String name, String description, NetaTemplateType type, NetaTemplateStatus status, String previewImageUrl, String fileUrl, String version, long downloads, double ratingAverage, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = Objects.requireNonNull(id, "El id es obligatorio");
        this.authorUserId = Objects.requireNonNull(authorUserId, "El autor es obligatorio");
        this.name = requireText(name, "El nombre es obligatorio");
        this.description = description == null ? "" : description.trim();
        this.type = Objects.requireNonNull(type, "El tipo es obligatorio");
        this.status = status == null ? NetaTemplateStatus.PENDING_REVIEW : status;
        this.previewImageUrl = previewImageUrl == null ? "" : previewImageUrl.trim();
        this.fileUrl = requireText(fileUrl, "La URL del archivo es obligatoria");
        this.version = version == null || version.isBlank() ? "1.0.0" : version.trim();
        this.downloads = Math.max(0, downloads);
        this.ratingAverage = Math.max(0, ratingAverage);
        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
        this.updatedAt = updatedAt == null ? this.createdAt : updatedAt;
    }

    public static NetaTemplate publish(UUID authorUserId, String name, String description, NetaTemplateType type, String previewImageUrl, String fileUrl, String version) {
        return new NetaTemplate(UUID.randomUUID(), authorUserId, name, description, type, NetaTemplateStatus.APPROVED, previewImageUrl, fileUrl, version, 0, 0, LocalDateTime.now(), LocalDateTime.now());
    }

    public static NetaTemplate rehydrate(UUID id, UUID authorUserId, String name, String description, NetaTemplateType type, NetaTemplateStatus status, String previewImageUrl, String fileUrl, String version, long downloads, double ratingAverage, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new NetaTemplate(id, authorUserId, name, description, type, status, previewImageUrl, fileUrl, version, downloads, ratingAverage, createdAt, updatedAt);
    }

    public void registerDownload() {
        downloads++;
        updatedAt = LocalDateTime.now();
    }

    public UUID getId() { return id; }
    public UUID getAuthorUserId() { return authorUserId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public NetaTemplateType getType() { return type; }
    public NetaTemplateStatus getStatus() { return status; }
    public String getPreviewImageUrl() { return previewImageUrl; }
    public String getFileUrl() { return fileUrl; }
    public String getVersion() { return version; }
    public long getDownloads() { return downloads; }
    public double getRatingAverage() { return ratingAverage; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    private static String requireText(String value, String message) {
        if (value == null || value.trim().isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
