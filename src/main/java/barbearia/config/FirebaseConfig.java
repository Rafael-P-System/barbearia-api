package barbearia.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void initialize() {
        try {
            InputStream serviceAccount;
            
            // 1. Tenta buscar o conteúdo do JSON direto da variável de ambiente (Produção)
            String firebaseJson = System.getenv("FIREBASE_CONFIG_JSON");
            
            if (firebaseJson != null && !firebaseJson.isEmpty()) {
                serviceAccount = new ByteArrayInputStream(firebaseJson.getBytes(StandardCharsets.UTF_8));
            } else {
                // 2. Se não achar a variável, busca o arquivo local (Desenvolvimento)
                serviceAccount = getClass().getClassLoader().getResourceAsStream("firebase-config.json");
            }

            if (serviceAccount == null) {
                throw new RuntimeException("Configuração do Firebase não encontrada localmente nem no ambiente.");
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("✅ Firebase inicializado com sucesso!");
            }
        } catch (Exception e) {
            System.err.println("❌ Erro ao inicializar o Firebase: " + e.getMessage());
        }
    }
}