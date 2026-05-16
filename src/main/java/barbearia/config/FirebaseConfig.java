package barbearia.config;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.auth.oauth2.GoogleCredentials;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void initialize() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                String firebaseJson = System.getenv("FIREBASE_CONFIG_JSON");

                if (firebaseJson == null || firebaseJson.trim().isEmpty()) {
                    System.err.println("❌ [ERRO] A variável de ambiente FIREBASE_CONFIG_JSON está vazia ou ausente!");
                    return;
                }

                String cleanJson = firebaseJson.trim();
                
                FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(
                        new ByteArrayInputStream(cleanJson.getBytes(StandardCharsets.UTF_8))))
                    .build();

                FirebaseApp.initializeApp(options);
                System.out.println("🚀 [SUCESSO] Firebase inicializado com sucesso via variável de ambiente!");
            }
        } catch (Exception e) {
            System.err.println("❌ [ERRO CRÍTICO] Falha ao processar o JSON do Firebase: " + e.getMessage());
            e.printStackTrace();
        }
    }
}