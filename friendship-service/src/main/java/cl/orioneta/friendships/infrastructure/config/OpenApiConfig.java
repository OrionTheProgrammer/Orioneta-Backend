package cl.orioneta.friendships.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Metadatos de Swagger/OpenAPI para friendship-service.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI friendshipServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Orioneta Friendship Service API")
                        .description("API para solicitudes de amistad, amigos y bloqueos.")
                        .version("1.0.0"));
    }
}
