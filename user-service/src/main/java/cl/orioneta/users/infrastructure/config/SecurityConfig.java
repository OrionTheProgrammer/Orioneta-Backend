package cl.orioneta.users.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuracion de seguridad del user-service.
 *
 * <p>Durante el MVP los endpoints quedan abiertos para probar rapido con
 * Swagger/Postman. Cuando auth-service emita JWT reales, se puede cambiar
 * {@code permitAll()} por {@code authenticated()} y activar Resource Server.</p>
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
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/actuator/**"
                        ).permitAll()
                        .anyRequest().permitAll()
                );

        return http.build();
    }
}
