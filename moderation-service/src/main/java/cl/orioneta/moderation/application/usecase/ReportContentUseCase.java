package cl.orioneta.moderation.application.usecase;

import cl.orioneta.moderation.application.dto.ModerationReviewRequestDTO;
import cl.orioneta.moderation.domain.model.ModerationReview;
import cl.orioneta.moderation.domain.repository.ModerationReviewRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class ReportContentUseCase {

    private final ModerationReviewRepositoryPort repository;

    public ReportContentUseCase(ModerationReviewRepositoryPort repository) {
        this.repository = repository;
    }

    public ModerationReview execute(ModerationReviewRequestDTO request) {
        return repository.save(ModerationReview.create(request.targetId(), request.targetType(), request.reason()));
    }
}
