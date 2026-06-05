package cl.orioneta.audit.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI auditServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Orioneta Audit Service API")
                        .description("API para registrar y consultar trazabilidad")
                        .version("1.0.0"));
    }
}
