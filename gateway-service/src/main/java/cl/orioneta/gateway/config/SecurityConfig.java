package cl.orioneta.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Seguridad HTTP del gateway.
 *
 * <p>Durante el MVP se dejan pasar las rutas para facilitar pruebas locales.
 * Cuando auth-service este integrado en el frontend, este archivo es el punto
 * donde se endurece el gateway con {@code oauth2ResourceServer().jwt()}.</p>
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/actuator/**",
                                "/api/auth/**",
                                "/oauth2/**",
                                "/login/oauth2/**"
                        ).permitAll()
                        .anyRequest().permitAll()
                );

        return http.build();
    }
}
