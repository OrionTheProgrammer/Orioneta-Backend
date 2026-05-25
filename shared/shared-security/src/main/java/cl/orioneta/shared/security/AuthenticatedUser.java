package cl.orioneta.shared.security;

import java.util.Set;
import java.util.UUID;

public record AuthenticatedUser(
        UUID id,
        String email,
        Set<String> roles
) {
}
