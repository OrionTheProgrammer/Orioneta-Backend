package cl.orioneta.moderation.application.mapper;

import cl.orioneta.moderation.application.dto.ModerationReviewResponseDTO;
import cl.orioneta.moderation.domain.model.ModerationReview;
import org.springframework.stereotype.Component;

@Component
public class ModerationMapper {

    public ModerationReviewResponseDTO toResponse(ModerationReview review) {
        return new ModerationReviewResponseDTO(
                review.getId(),
                review.getTargetId(),
                review.getTargetType(),
                review.getReviewerId(),
                review.getStatus(),
                review.getReason(),
                review.getCreatedAt(),
                review.getResolvedAt()
        );
    }
}
