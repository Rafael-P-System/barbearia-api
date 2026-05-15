package barbearia.config;

import barbearia.security.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class securityconfig {

    private final JwtFilter jwtFilter;

    public securityconfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            // 🔒 desativa csrf (API stateless)
            .csrf(csrf -> csrf.disable())

            // 🌐 ativa CORS
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            // 🔐 sem sessão (JWT)
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // 🔓 rotas públicas (Corrigido o padrão do Spring Boot 3)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/api/login",
                    "/api/cadastro",
                    "/api/auth/**",      // Permite tudo que comece com /api/auth/
                    "/v3/api-docs/**",   // Documentação Swagger (caso use)
                    "/swagger-ui/**"     // Interface Swagger (caso use)
                ).permitAll()
                .anyRequest().authenticated()
            )

            // 🔥 filtro JWT antes do auth padrão
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        // ❌ desativa login padrão do Spring
        http.formLogin(form -> form.disable());
        http.httpBasic(basic -> basic.disable());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Adicionei "*" apenas para teste, depois você volta para seus IPs específicos
        configuration.setAllowedOrigins(List.of(
            "http://localhost:8081",
            "http://192.168.10.11:8081"
        ));

        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}