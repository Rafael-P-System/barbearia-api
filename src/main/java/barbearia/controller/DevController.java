package barbearia.controller;

import barbearia.entity.Barbearia;
import barbearia.repository.BarbeariaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/dev")
@CrossOrigin("*")
public class DevController {

    // 🔐 agora vem do application.properties
    @Value("${dev.owner.key}")
    private String ownerKey;

    @Autowired
    private BarbeariaRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // =========================
    // 👑 CRIAR ADMIN (SÓ DEV)
    // =========================
    @PostMapping("/criar-admin")
    public ResponseEntity<?> criarAdmin(
            @RequestHeader(value = "X-OWNER-KEY", required = false) String key,
            @RequestBody Barbearia admin
    ) {

        // 🔐 valida chave
        if (key == null || !ownerKey.equals(key)) {
            return ResponseEntity.status(403)
                    .body(Map.of("erro", "ACESSO NEGADO"));
        }

        // ❌ valida email
        if (admin.getEmail() == null || admin.getEmail().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("erro", "EMAIL OBRIGATÓRIO"));
        }

        // ❌ verifica duplicado
        if (repository.findByEmail(admin.getEmail()) != null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("erro", "EMAIL JÁ EXISTE"));
        }

        // 👑 define ADMIN
        admin.setNivel("ADMIN");
        
        // 🔥 CORREÇÃO AQUI: Mudado de setAtivo(true) para setStatus("ATIVO")
        // para alinhar com a String do banco de dados e com o Front-End SaaS.
        admin.setStatus("ATIVO");

        // 🔒 senha segura
        admin.setSenha(passwordEncoder.encode(admin.getSenha()));

        Barbearia salvo = repository.save(admin);

        return ResponseEntity.ok(Map.of(
                "mensagem", "ADMIN CRIADO COM SUCESSO",
                "id", salvo.getId(),
                "email", salvo.getEmail()
        ));
    }
}