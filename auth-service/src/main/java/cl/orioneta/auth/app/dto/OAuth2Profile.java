package cl.orioneta.auth.app.dto;

import cl.orioneta.auth.domain.model.AuthProvider;

/**
 * Identidad externa verificada por Google o GitHub antes de emitir JWT propio.
 */
public record OAuth2Profile(
        AuthProvider provider,
        String providerUserId,
        String email,
        String displayName,
        String avatarUrl
) {
}
