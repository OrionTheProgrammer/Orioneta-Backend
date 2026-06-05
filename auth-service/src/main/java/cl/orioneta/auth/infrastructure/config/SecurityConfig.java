package cl.orioneta.auth.infrastructure.config;

import cl.orioneta.auth.infrastructure.security.OAuth2LoginSuccessHandler;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.util.UriUtils;

/**
 * Configuracion HTTP del auth-service.
 *
 * <p>El registro/login por email es REST puro. El login con Google/GitHub usa
 * el flujo OAuth2 de Spring Security solo para comprobar la identidad externa;
 * cuando el proveedor responde bien, {@link OAuth2LoginSuccessHandler} genera
 * una sesion propia de Orioneta.</p>
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final OAuth2LoginSuccessHandler oauth2LoginSuccessHandler;
    private final String oauth2FailureRedirectUri;

    public SecurityConfig(
            OAuth2LoginSuccessHandler oauth2LoginSuccessHandler,
            @Value("${orioneta.auth.oauth2.failure-redirect-uri}") String oauth2FailureRedirectUri
    ) {
        this.oauth2LoginSuccessHandler = oauth2LoginSuccessHandler;
        this.oauth2FailureRedirectUri = oauth2FailureRedirectUri;
    }

    /**
     * Define que endpoints quedan publicos y como se maneja OAuth2 Login.
     *
     * <p>Los endpoints de autenticacion son publicos porque justamente sirven
     * para iniciar sesion. OAuth2 Login necesita sesion HTTP temporal para
     * conservar el estado entre la ida al proveedor y el callback.</p>
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/**",
                                "/oauth2/**",
                                "/login/oauth2/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/actuator/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(oauth2LoginSuccessHandler)
                        .failureHandler((request, response, exception) -> redirectOAuth2Failure(response, exception))
                );

        return http.build();
    }

    private void redirectOAuth2Failure(HttpServletResponse response, Exception exception) throws IOException {
        String message = exception.getMessage() == null ? "oauth2_error" : exception.getMessage();
        response.sendRedirect(oauth2FailureRedirectUri + "?error=" + UriUtils.encode(message, StandardCharsets.UTF_8));
    }
}
