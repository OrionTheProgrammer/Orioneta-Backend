package cl.orioneta.moderation.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cl.orioneta.moderation.application.dto.ModerationReviewRequestDTO;
import cl.orioneta.moderation.domain.exception.ModerationReviewNotFoundException;
import cl.orioneta.moderation.domain.model.ModerationReview;
import cl.orioneta.moderation.domain.model.ModerationStatus;
import cl.orioneta.moderation.domain.model.ModerationTargetType;
import cl.orioneta.moderation.domain.repository.ModerationReviewRepositoryPort;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Pruebas de los casos de uso de moderacion.
 */
@ExtendWith(MockitoExtension.class)
class ModerationUseCaseTest {

    private final Faker faker = new Faker();

    @Mock
    private ModerationReviewRepositoryPort repository;

    private ReportContentUseCase reportUseCase;
    private ReviewTemplateUseCase reviewUseCase;

    @BeforeEach
    void setUp() {
        reportUseCase = new ReportContentUseCase(repository);
        reviewUseCase = new ReviewTemplateUseCase(repository);
    }

    @Test
    void reportCreatesPendingReview() {
        ModerationReviewRequestDTO request = new ModerationReviewRequestDTO(
                UUID.randomUUID(),
                ModerationTargetType.NETA_TEMPLATE,
                null,
                null,
                faker.lorem().sentence()
        );
        when(repository.save(any(ModerationReview.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ModerationReview review = reportUseCase.execute(request);

        verify(repository).save(any(ModerationReview.class));
        assertThat(review.getTargetId()).isEqualTo(request.targetId());
        assertThat(review.getStatus()).isEqualTo(ModerationStatus.PENDING);
    }

    @Test
    void resolveUpdatesExistingReview() {
        ModerationReview review = ModerationReview.create(
                UUID.randomUUID(),
                ModerationTargetType.NETA_TEMPLATE,
                "pendiente"
        );
        ModerationReviewRequestDTO request = new ModerationReviewRequestDTO(
                review.getTargetId(),
                review.getTargetType(),
                UUID.randomUUID(),
                ModerationStatus.REJECTED,
                "No cumple las reglas"
        );
        when(repository.findById(review.getId())).thenReturn(Optional.of(review));
        when(repository.save(any(ModerationReview.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ModerationReview resolved = reviewUseCase.resolve(review.getId(), request);

        assertThat(resolved.getStatus()).isEqualTo(ModerationStatus.REJECTED);
        assertThat(resolved.getReviewerId()).isEqualTo(request.reviewerId());
        assertThat(resolved.getResolvedAt()).isNotNull();
    }

    @Test
    void resolveFailsWhenReviewDoesNotExist() {
        UUID reviewId = UUID.randomUUID();
        ModerationReviewRequestDTO request = new ModerationReviewRequestDTO(
                UUID.randomUUID(),
                ModerationTargetType.NETA_TEMPLATE,
                UUID.randomUUID(),
                ModerationStatus.APPROVED,
                "ok"
        );
        when(repository.findById(reviewId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reviewUseCase.resolve(reviewId, request))
                .isInstanceOf(ModerationReviewNotFoundException.class)
                .hasMessage("Revision de moderacion no encontrada");
    }

    @Test
    void findPendingDelegatesToRepository() {
        List<ModerationReview> pending = List.of(ModerationReview.create(
                UUID.randomUUID(),
                ModerationTargetType.NETA_TEMPLATE,
                "pendiente"
        ));
        when(repository.findPending()).thenReturn(pending);

        assertThat(reviewUseCase.findPending()).containsExactlyElementsOf(pending);
    }
}
