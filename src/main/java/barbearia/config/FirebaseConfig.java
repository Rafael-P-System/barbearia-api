package barbearia.config;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.auth.oauth2.GoogleCredentials;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void initialize() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                // 1. Tenta buscar o JSON pela variável de ambiente do Render
                String firebaseJson = System.getenv("FIREBASE_CONFIG_JSON");
                FirebaseOptions options;

                if (firebaseJson != null && !firebaseJson.isEmpty()) {
                    // Se estiver no Render, lê direto da memória externa
                    options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(
                            new ByteArrayInputStream(firebaseJson.getBytes(StandardCharsets.UTF_8))))
                        .build();
                    System.out.println("✅ Firebase conectado com sucesso usando variável de ambiente (Render)!");
                } else {
                    // 2. Se estiver no seu PC (local), usa o arquivo físico que está na pasta resources
                    options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(
                            new ClassPathResource("firebase-config.json").getInputStream()))
                        .build();
                    System.out.println("✅ Firebase conectado com sucesso usando arquivo local!");
                }

                FirebaseApp.initializeApp(options);
            }
        } catch (IOException e) {
            System.err.println("❌ Erro ao conectar ao Firebase: " + e.getMessage());
        }
    }
}