package cl.orioneta.auth.infrastructure.security;

import cl.orioneta.auth.app.dto.OAuth2Profile;
import cl.orioneta.auth.domain.model.AuthProvider;
import java.util.Arrays;
import java.util.Map;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * Extrae una identidad normalizada desde Google o GitHub.
 */
@Component
public class OAuth2ProfileExtractor {

    private final OAuth2AuthorizedClientService authorizedClientService;
    private final RestClient restClient;

    public OAuth2ProfileExtractor(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
        this.restClient = RestClient.create();
    }

    public OAuth2Profile extract(OAuth2AuthenticationToken authentication) {
        String registrationId = authentication.getAuthorizedClientRegistrationId();
        Map<String, Object> attributes = authentication.getPrincipal().getAttributes();

        return switch (registrationId.toLowerCase()) {
            case "google" -> extractGoogle(attributes);
            case "github" -> extractGithub(authentication, attributes);
            default -> throw new IllegalArgumentException("Proveedor OAuth2 no soportado: " + registrationId);
        };
    }

    private OAuth2Profile extractGoogle(Map<String, Object> attributes) {
        Object emailVerified = attributes.get("email_verified");

        if (emailVerified != null && !Boolean.parseBoolean(emailVerified.toString())) {
            throw new IllegalArgumentException("Google no confirmo que el email este verificado");
        }

        return new OAuth2Profile(
                AuthProvider.GOOGLE,
                requireAttribute(attributes, "sub"),
                requireAttribute(attributes, "email"),
                valueOrFallback(attributes, "name", "Google User"),
                valueOrFallback(attributes, "picture", "")
        );
    }

    private OAuth2Profile extractGithub(OAuth2AuthenticationToken authentication, Map<String, Object> attributes) {
        String email = valueOrFallback(attributes, "email", "");

        if (email.isBlank()) {
            try {
                email = fetchPrimaryGithubEmail(authentication);
            } catch (Exception e) {
                // Fallback: usar login como identificador de email
                String login = valueOrFallback(attributes, "login", "unknown");
                email = login + "@github.noreply.com";
            }
        }

        return new OAuth2Profile(
                AuthProvider.GITHUB,
                String.valueOf(attributes.get("id")),
                email,
                valueOrFallback(attributes, "name", valueOrFallback(attributes, "login", "GitHub User")),
                valueOrFallback(attributes, "avatar_url", "")
        );
    }

    private String fetchPrimaryGithubEmail(OAuth2AuthenticationToken authentication) {
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                authentication.getAuthorizedClientRegistrationId(),
                authentication.getName()
        );

        if (client == null || client.getAccessToken() == null) {
            throw new IllegalArgumentException("No se pudo obtener el access token de GitHub");
        }

        GitHubEmail[] emails = restClient.get()
                .uri("https://api.github.com/user/emails")
                .header("Authorization", "Bearer " + client.getAccessToken().getTokenValue())
                .retrieve()
                .body(GitHubEmail[].class);

        return Arrays.stream(emails == null ? new GitHubEmail[0] : emails)
                .filter(email -> Boolean.TRUE.equals(email.primary()))
                .filter(email -> Boolean.TRUE.equals(email.verified()))
                .map(GitHubEmail::email)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("GitHub no entrego un email primario verificado"));
    }

    private String requireAttribute(Map<String, Object> attributes, String key) {
        String value = valueOrFallback(attributes, key, "");

        if (value.isBlank()) {
            throw new IllegalArgumentException("OAuth2 no entrego el atributo requerido: " + key);
        }

        return value;
    }

    private String valueOrFallback(Map<String, Object> attributes, String key, String fallback) {
        Object value = attributes.get(key);
        return value == null ? fallback : value.toString();
    }

    private record GitHubEmail(String email, Boolean primary, Boolean verified) {
    }
}
