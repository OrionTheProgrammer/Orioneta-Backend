package cl.orioneta.shared.kernel.response;

import java.time.Instant;
import java.util.List;

public record ErrorResponse(
        String code,
        String message,
        List<String> details,
        Instant timestamp
) {

    public static ErrorResponse of(String code, String message) {
        return new ErrorResponse(code, message, List.of(), Instant.now());
    }
}
