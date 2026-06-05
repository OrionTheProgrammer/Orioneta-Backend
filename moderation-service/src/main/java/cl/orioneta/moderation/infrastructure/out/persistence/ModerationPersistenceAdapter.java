package cl.orioneta.moderation.infrastructure.out.persistence;

import cl.orioneta.moderation.domain.model.ModerationReview;
import cl.orioneta.moderation.domain.model.ModerationStatus;
import cl.orioneta.moderation.domain.repository.ModerationReviewRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ModerationPersistenceAdapter implements ModerationReviewRepositoryPort {

    private final JpaModerationReviewRepository repository;

    public ModerationPersistenceAdapter(JpaModerationReviewRepository repository) {
        this.repository = repository;
    }

    @Override
    public ModerationReview save(ModerationReview review) {
        return toDomain(repository.save(toEntity(review)));
    }

    @Override
    public Optional<ModerationReview> findById(UUID id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public List<ModerationReview> findPending() {
        return repository.findByStatusOrderByCreatedAtAsc(ModerationStatus.PENDING)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    private ModerationReviewEntity toEntity(ModerationReview review) {
        return new ModerationReviewEntity(review.getId(), review.getTargetId(), review.getTargetType(), review.getReviewerId(), review.getStatus(), review.getReason(), review.getCreatedAt(), review.getResolvedAt());
    }

    private ModerationReview toDomain(ModerationReviewEntity entity) {
        return ModerationReview.rehydrate(entity.getId(), entity.getTargetId(), entity.getTargetType(), entity.getReviewerId(), entity.getStatus(), entity.getReason(), entity.getCreatedAt(), entity.getResolvedAt());
    }
}
