package cl.orioneta.media.infrastructure.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaMediaRepository extends JpaRepository<MediaEntity, UUID> {

    List<MediaEntity> findByOwnerUserIdAndDeletedAtIsNullOrderByCreatedAtDesc(UUID ownerUserId);
}
