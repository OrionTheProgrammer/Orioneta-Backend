package cl.orioneta.moderation.infrastructure.out.persistence;

import cl.orioneta.moderation.domain.model.ModerationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaModerationReviewRepository extends JpaRepository<ModerationReviewEntity, UUID> {

    List<ModerationReviewEntity> findByStatusOrderByCreatedAtAsc(ModerationStatus status);
}
