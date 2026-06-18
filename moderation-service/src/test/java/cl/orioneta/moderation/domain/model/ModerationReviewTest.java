package cl.orioneta.moderation.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.UUID;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;

/**
 * Pruebas de reglas puras de moderacion.
 */
class ModerationReviewTest {

    private final Faker faker = new Faker();

    @Test
    void createStartsAsPendingWithoutReviewer() {
        ModerationReview review = ModerationReview.create(
                UUID.randomUUID(),
                ModerationTargetType.NETA_TEMPLATE,
                faker.lorem().sentence()
        );

        assertThat(review.getStatus()).isEqualTo(ModerationStatus.PENDING);
        assertThat(review.getReviewerId()).isNull();
        assertThat(review.getResolvedAt()).isNull();
    }

    @Test
    void resolveStoresReviewerStatusReasonAndDate() {
        ModerationReview review = ModerationReview.create(
                UUID.randomUUID(),
                ModerationTargetType.NETA_TEMPLATE,
                "pendiente"
        );
        UUID reviewerId = UUID.randomUUID();

        review.resolve(reviewerId, ModerationStatus.APPROVED, " aprobado ");

        assertThat(review.getReviewerId()).isEqualTo(reviewerId);
        assertThat(review.getStatus()).isEqualTo(ModerationStatus.APPROVED);
        assertThat(review.getReason()).isEqualTo("aprobado");
        assertThat(review.getResolvedAt()).isNotNull();
    }

    @Test
    void resolveRejectsPendingStatus() {
        ModerationReview review = ModerationReview.create(
                UUID.randomUUID(),
                ModerationTargetType.NETA_TEMPLATE,
                "pendiente"
        );

        assertThatThrownBy(() -> review.resolve(UUID.randomUUID(), ModerationStatus.PENDING, "sin cambios"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("La resolucion no puede quedar pendiente");
    }
}
