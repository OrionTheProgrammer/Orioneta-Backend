package cl.orioneta.media.infrastructure.out.persistence;

import cl.orioneta.media.domain.model.MediaFile;
import cl.orioneta.media.domain.repository.MediaRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class MediaPersistenceAdapter implements MediaRepositoryPort {

    private final JpaMediaRepository jpaMediaRepository;

    public MediaPersistenceAdapter(JpaMediaRepository jpaMediaRepository) {
        this.jpaMediaRepository = jpaMediaRepository;
    }

    @Override
    public MediaFile save(MediaFile mediaFile) {
        return toDomain(jpaMediaRepository.save(toEntity(mediaFile)));
    }

    @Override
    public Optional<MediaFile> findById(UUID id) {
        return jpaMediaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<MediaFile> findByOwnerUserId(UUID ownerUserId) {
        return jpaMediaRepository.findByOwnerUserIdAndDeletedAtIsNullOrderByCreatedAtDesc(ownerUserId)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    private MediaEntity toEntity(MediaFile mediaFile) {
        return new MediaEntity(
                mediaFile.getId(),
                mediaFile.getOwnerUserId(),
                mediaFile.getFileName(),
                mediaFile.getContentType(),
                mediaFile.getSize(),
                mediaFile.getUrl(),
                mediaFile.getPurpose(),
                mediaFile.getCreatedAt(),
                mediaFile.getDeletedAt()
        );
    }

    private MediaFile toDomain(MediaEntity entity) {
        return MediaFile.rehydrate(
                entity.getId(),
                entity.getOwnerUserId(),
                entity.getFileName(),
                entity.getContentType(),
                entity.getSize(),
                entity.getUrl(),
                entity.getPurpose(),
                entity.getCreatedAt(),
                entity.getDeletedAt()
        );
    }
}
