package cl.orioneta.users.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuracion de seguridad del user-service.
 *
 * <p>Durante el MVP deja abiertos los endpoints para facilitar pruebas con
 * Swagger y Postman. Cuando {@code auth-service} emita JWT reales, se debe
 * cambiar {@code permitAll()} por {@code authenticated()} y activar Resource
 * Server con JWT.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Construye la cadena de filtros HTTP usada por Spring Security.
     *
     * @param http builder de seguridad HTTP
     * @return cadena de filtros configurada
     * @throws Exception si Spring Security no puede construir la cadena
     */
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
