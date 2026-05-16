package barbearia.config;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.auth.oauth2.GoogleCredentials;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
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

                if (firebaseJson != null && !firebaseJson.isEmpty()) {
                    options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(
                            new ByteArrayInputStream(firebaseJson.getBytes(StandardCharsets.UTF_8))))
                        .build();
                    System.out.println("✅ Firebase conectado com sucesso usando variável de ambiente (Render)!");
                } else {
                    // Tenta ler o arquivo na raiz (Render Secret File ou Local Root)
                    FileSystemResource fileResource = new FileSystemResource("firebase-config.json");
                    InputStream stream;

                    if (fileResource.exists()) {
                        stream = fileResource.getInputStream();
                        System.out.println("✅ Firebase conectado com sucesso usando arquivo externo na raiz!");
                    } else {
                        // Backup caso esteja rodando local na estrutura antiga
                        stream = new ClassPathResource("firebase-config.json").getInputStream();
                        System.out.println("✅ Firebase conectado com sucesso usando arquivo do classpath local!");
                    }

                    options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(stream))
                        .build();
                }

                FirebaseApp.initializeApp(options);
            }
        } catch (IOException e) {
            System.err.println("❌ Erro ao conectar ao Firebase: " + e.getMessage());
        }
    }
}