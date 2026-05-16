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
                // 1. Tenta ler a String bruta direto da variável de ambiente (Para o Render)
                String firebaseJson = System.getenv("FIREBASE_CONFIG_JSON");
                FirebaseOptions options;

                if (firebaseJson != null && !firebaseJson.trim().isEmpty()) {
                    options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(
                            new ByteArrayInputStream(firebaseJson.getBytes(StandardCharsets.UTF_8))))
                        .build();
                    System.out.println("✅ Firebase conectado com sucesso usando variável de ambiente (Render)!");
                } else {
                    // 2. Se a variável estiver vazia, lê o arquivo físico do classpath (Para o seu PC local)
                    ClassPathResource resource = new ClassPathResource("firebase-config.json");
                    if (!resource.exists()) {
                        throw new IOException("Arquivo firebase-config.json não foi encontrado no classpath local.");
                    }
                    InputStream stream = resource.getInputStream();
                    options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(stream))
                        .build();
                    System.out.println("✅ Firebase conectado com sucesso usando arquivo local!");
                }

                FirebaseApp.initializeApp(options);
            }
        } catch (IOException e) {
            System.err.println("❌ Erro crítico ao inicializar o Firebase: " + e.getMessage());
        }
    }
}