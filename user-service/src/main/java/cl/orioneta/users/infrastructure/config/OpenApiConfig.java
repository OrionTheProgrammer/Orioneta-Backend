package cl.orioneta.users.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Metadatos de Swagger/OpenAPI para el user-service.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI userServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Orioneta User Service API")
                        .description("API para perfiles publicos, friend codes y presencia de usuarios.")
                        .version("1.0.0"));
    }
}
