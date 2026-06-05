package cl.orioneta.bff.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI bffServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Orioneta BFF API")
                        .description("API agregadora para vistas del frontend de Orioneta")
                        .version("1.0.0"));
    }
}
