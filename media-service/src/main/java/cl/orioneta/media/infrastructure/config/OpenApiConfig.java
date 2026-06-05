package cl.orioneta.media.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI mediaServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Orioneta Media Service API")
                        .description("API para registrar y consultar metadatos multimedia")
                        .version("1.0.0"));
    }
}
