package barbearia.config;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.auth.oauth2.GoogleCredentials;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void initialize() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                String firebaseJson = System.getenv("FIREBASE_CONFIG_JSON");
                FirebaseOptions options;

                // .trim() aprimorado para remover possíveis aspas ou espaços que o Render injeta
                if (firebaseJson != null && !firebaseJson.trim().isEmpty()) {
                    String cleanJson = firebaseJson.trim();
                    options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(
                            new ByteArrayInputStream(cleanJson.getBytes(StandardCharsets.UTF_8))))
                        .build();
                    System.out.println("✅ [DEBUG] Variável FIREBASE_CONFIG_JSON detectada. Tentando inicializar...");
                } else {
                    System.out.println("⚠️ [DEBUG] Variável de ambiente não encontrada. Mudando para o modo local...");
                    ClassPathResource resource = new ClassPathResource("firebase-config.json");
                    if (!resource.exists()) {
                        throw new IOException("Arquivo firebase-config.json não existe no classpath e a env local está vazia.");
                    }
                    InputStream stream = resource.getInputStream();
                    options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(stream))
                        .build();
                }

                FirebaseApp.initializeApp(options);
                System.out.println("🚀 [SUCESSO] FirebaseApp inicializado com êxito!");
            }
        } catch (Exception e) {
            System.err.println("❌ [ERRO COMPLETO] Falha na inicialização do Firebase: " + e.getMessage());
            e.printStackTrace();
        }
    }
}