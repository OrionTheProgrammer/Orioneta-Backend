package cl.orioneta.customization.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customizationServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Orioneta Customization Service API")
                        .description("API para preferencias visuales globales y por conversacion")
                        .version("1.0.0"));
    }
}
