package cl.orioneta.customization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class CustomizationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomizationServiceApplication.class, args);
    }
}
