package cl.orioneta.moderation.domain.exception;

public class ModerationReviewNotFoundException extends RuntimeException {

    public ModerationReviewNotFoundException(String message) {
        super(message);
    }
}
