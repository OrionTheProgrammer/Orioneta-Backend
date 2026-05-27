package cl.orioneta.moderation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class ModerationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ModerationServiceApplication.class, args);
    }
}
