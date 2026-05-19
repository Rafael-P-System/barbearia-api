package barbearia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

// ❌ Removemos o scanBasePackages travado e desativamos a autoconfiguração de segurança
@SpringBootApplication(exclude = { 
    SecurityAutoConfiguration.class, 
    UserDetailsServiceAutoConfiguration.class 
})
@EnableJpaRepositories(basePackages = "barbearia.repository")
@EntityScan(basePackages = "barbearia.entity")
public class BarbeariaApplication {
    public static void main(String[] args) {
        SpringApplication.run(BarbeariaApplication.class, args);
    }
}