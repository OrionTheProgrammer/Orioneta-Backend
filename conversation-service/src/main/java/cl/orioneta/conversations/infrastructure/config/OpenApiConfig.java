package cl.orioneta.conversations.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI conversationServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Orioneta Conversation Service API")
                        .description("API para chats privados, grupos y participantes")
                        .version("1.0.0"));
    }
}
