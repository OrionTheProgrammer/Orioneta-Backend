package cl.orioneta.auth.infrastructure.security;

import cl.orioneta.auth.app.dto.AuthResponse;
import cl.orioneta.auth.app.dto.OAuth2Profile;
import cl.orioneta.auth.app.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriUtils;

/**
 * Convierte un login externo exitoso en una sesion interna de Orioneta.
 */
@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final AuthService authService;
    private final OAuth2ProfileExtractor profileExtractor;
    private final String successRedirectUri;

    public OAuth2LoginSuccessHandler(
            AuthService authService,
            OAuth2ProfileExtractor profileExtractor,
            @Value("${orioneta.auth.oauth2.success-redirect-uri}") String successRedirectUri
    ) {
        this.authService = authService;
        this.profileExtractor = profileExtractor;
        this.successRedirectUri = successRedirectUri;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) authentication;
        OAuth2Profile profile = profileExtractor.extract(oauth2Token);
        AuthResponse authResponse = authService.loginWithOAuth2(profile);

        response.sendRedirect(successRedirectUri + "#" + buildFragment(authResponse));
    }

    private String buildFragment(AuthResponse authResponse) {
        return "accessToken=" + encode(authResponse.accessToken())
                + "&refreshToken=" + encode(authResponse.refreshToken())
                + "&tokenType=" + encode(authResponse.tokenType())
                + "&expiresIn=" + authResponse.expiresInSeconds()
                + "&userId=" + authResponse.userId()
                + "&email=" + encode(authResponse.email());
    }

    private String encode(String value) {
        return UriUtils.encode(value, StandardCharsets.UTF_8);
    }
}
