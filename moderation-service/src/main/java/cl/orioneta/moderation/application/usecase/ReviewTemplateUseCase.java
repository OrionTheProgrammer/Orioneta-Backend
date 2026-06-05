package cl.orioneta.moderation.application.usecase;

import cl.orioneta.moderation.application.dto.ModerationReviewRequestDTO;
import cl.orioneta.moderation.domain.exception.ModerationReviewNotFoundException;
import cl.orioneta.moderation.domain.model.ModerationReview;
import cl.orioneta.moderation.domain.repository.ModerationReviewRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ReviewTemplateUseCase {

    private final ModerationReviewRepositoryPort repository;

    public ReviewTemplateUseCase(ModerationReviewRepositoryPort repository) {
        this.repository = repository;
    }

    public ModerationReview resolve(UUID id, ModerationReviewRequestDTO request) {
        ModerationReview review = repository.findById(id)
                .orElseThrow(() -> new ModerationReviewNotFoundException("Revision de moderacion no encontrada"));
        review.resolve(request.reviewerId(), request.status(), request.reason());
        return repository.save(review);
    }

    public List<ModerationReview> findPending() {
        return repository.findPending();
    }
}
