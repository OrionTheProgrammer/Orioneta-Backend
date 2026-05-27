package cl.orioneta.friendships;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class FriendshipServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FriendshipServiceApplication.class, args);
    }
}
