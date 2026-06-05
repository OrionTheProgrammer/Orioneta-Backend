package cl.orioneta.media.domain.repository;

import cl.orioneta.media.domain.model.MediaFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MediaRepositoryPort {

    MediaFile save(MediaFile mediaFile);

    Optional<MediaFile> findById(UUID id);

    List<MediaFile> findByOwnerUserId(UUID ownerUserId);
}
