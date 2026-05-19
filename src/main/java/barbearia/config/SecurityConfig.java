package barbearia.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // ❌ Desativa explicitamente os mecanismos de login padrão que geram a senha automática
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable())
            
            // 🌐 Ativa o CORS com as regras definidas no método abaixo
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // 🛡️ Mantém o CSRF desativado para o padrão de APIs REST
            .csrf(csrf -> csrf.disable())
            
            // 🔓 Mantém todos os endpoints liberados temporariamente para os testes
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Libera localhost e qualquer deploy/subdomínio da Vercel de forma dinâmica
        configuration.setAllowedOriginPatterns(Arrays.asList(
            "http://localhost:*",
            "http://127.0.0.1:*",
            "https://*.vercel.app"
        ));

        // Métodos HTTP permitidos para as operações do aplicativo
        configuration.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "OPTIONS"
        ));

        // Todos os cabeçalhos liberados (evita travamento com Axios)
        configuration.setAllowedHeaders(Arrays.asList("*"));

        // Permite envio de credenciais, tokens ou cookies se necessário
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}