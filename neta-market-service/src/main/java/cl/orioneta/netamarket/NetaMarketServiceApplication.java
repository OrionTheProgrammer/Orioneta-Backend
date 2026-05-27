package cl.orioneta.netamarket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class NetaMarketServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NetaMarketServiceApplication.class, args);
    }
}
