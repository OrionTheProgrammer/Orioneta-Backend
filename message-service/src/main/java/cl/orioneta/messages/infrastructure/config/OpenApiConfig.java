package cl.orioneta.messages.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI messageServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Orioneta Message Service API")
                        .description("API para envio, lectura, edicion y eliminacion logica de mensajes")
                        .version("1.0.0"));
    }
}
