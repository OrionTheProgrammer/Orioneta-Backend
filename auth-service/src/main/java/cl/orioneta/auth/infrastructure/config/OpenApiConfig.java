package cl.orioneta.auth.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuracion OpenAPI del auth-service.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI authServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Orioneta Auth Service API")
                        .description("API para registro, login, refresh token y OAuth2 con JWT propio de Orioneta")
                        .version("1.0.0"));
    }
}
