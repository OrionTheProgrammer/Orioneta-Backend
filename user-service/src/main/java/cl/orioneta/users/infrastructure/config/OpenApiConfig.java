package cl.orioneta.users.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuracion OpenAPI para documentar endpoints del user-service.
 */
@Configuration
public class OpenApiConfig {

    /**
     * Define la metadata visible en Swagger UI.
     *
     * @return configuracion OpenAPI del servicio
     */
    @Bean
    public OpenAPI userServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Orioneta User Service API")
                        .description("API para gestion de usuarios, perfiles y friend codes de Orioneta")
                        .version("1.0.0"));
    }
}
