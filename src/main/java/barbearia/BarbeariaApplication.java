package barbearia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "barbearia") // <--- ISSO AQUI É A CHAVE
@EnableJpaRepositories(basePackages = "barbearia.repository")
@EntityScan(basePackages = "barbearia.entity")
public class BarbeariaApplication {
    public static void main(String[] args) {
        SpringApplication.run(BarbeariaApplication.class, args);
    }
}