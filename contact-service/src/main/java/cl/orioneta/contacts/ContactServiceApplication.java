package cl.orioneta.contacts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class ContactServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContactServiceApplication.class, args);
    }
}
