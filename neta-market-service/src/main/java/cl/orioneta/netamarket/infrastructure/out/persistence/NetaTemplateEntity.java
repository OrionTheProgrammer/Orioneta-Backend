package cl.orioneta.netamarket.infrastructure.out.persistence;

import cl.orioneta.netamarket.domain.model.NetaTemplateStatus;
import cl.orioneta.netamarket.domain.model.NetaTemplateType;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "neta_templates")
public class NetaTemplateEntity {

    @Id
    private UUID id;
    @Column(name = "author_user_id", nullable = false)
    private UUID authorUserId;
    @Column(nullable = false, length = 120)
    private String name;
    @Column(length = 500)
    private String description;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private NetaTemplateType type;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private NetaTemplateStatus status;
    @Column(name = "preview_image_url", length = 1000)
    private String previewImageUrl;
    @Column(name = "file_url", nullable = false, length = 1000)
    private String fileUrl;
    @Column(nullable = false, length = 40)
    private String version;
    @Column(nullable = false)
    private long downloads;
    @Column(name = "rating_average", nullable = false)
    private double ratingAverage;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected NetaTemplateEntity() {
    }

    public NetaTemplateEntity(UUID id, UUID authorUserId, String name, String description, NetaTemplateType type, NetaTemplateStatus status, String previewImageUrl, String fileUrl, String version, long downloads, double ratingAverage, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.authorUserId = authorUserId;
        this.name = name;
        this.description = description;
        this.type = type;
        this.status = status;
        this.previewImageUrl = previewImageUrl;
        this.fileUrl = fileUrl;
        this.version = version;
        this.downloads = downloads;
        this.ratingAverage = ratingAverage;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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
}
