package cl.orioneta.netamarket.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI netaMarketServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Orioneta Neta Market API")
                        .description("API para publicar, buscar y descargar templates visuales")
                        .version("1.0.0"));
    }
}
