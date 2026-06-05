package cl.orioneta.moderation.domain.repository;

import cl.orioneta.moderation.domain.model.ModerationReview;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ModerationReviewRepositoryPort {

    ModerationReview save(ModerationReview review);

    Optional<ModerationReview> findById(UUID id);

    List<ModerationReview> findPending();
}
