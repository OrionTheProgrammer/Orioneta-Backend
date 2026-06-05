package cl.orioneta.auth.app.dto;

/**
 * Datos publicos para iniciar login OAuth2 desde el frontend.
 */
public record OAuth2ProviderResponse(
        String provider,
        String authorizationUrl
) {
}
