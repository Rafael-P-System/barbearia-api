package barbearia.controller;

import barbearia.entity.Barbearia;
import barbearia.repository.BarbeariaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private BarbeariaRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // =========================
    // LOGIN
    // =========================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {

        String email = body.get("email");
        String senha = body.get("senha");

        Barbearia usuario = repository.findByEmail(email);

        if (usuario == null) {
            return ResponseEntity.status(404)
                    .body(Map.of("erro", "USUÁRIO NÃO ENCONTRADO"));
        }

        boolean senhaCorreta =
                passwordEncoder.matches(senha, usuario.getSenha());

        if (!senhaCorreta) {
            return ResponseEntity.status(401)
                    .body(Map.of("erro", "SENHA INCORRETA"));
        }

        return ResponseEntity.ok(Map.of(
                "mensagem", "LOGIN OK",
                "id", usuario.getId(),
                "nome", usuario.getNome(),
                "email", usuario.getEmail(),
                "nivel", usuario.getNivel(),
                "status", usuario.getStatus()
        ));
    }

    // =========================
    // LISTAR BARBEARIAS
    // =========================
    @GetMapping("/barbearias")
    public List<Barbearia> listarTodas() {

        return repository.findAll().stream()
                .filter(b -> "ADM".equals(b.getNivel()))
                .collect(Collectors.toList());
    }

    // =========================
    // ALTERAR STATUS
    // =========================
    @PutMapping("/barbearia/{id}/status")
    public ResponseEntity<?> alterarStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body
    ) {

        String novoStatus = body.get("status");

        if (novoStatus == null ||
                (!novoStatus.equals("ATIVO")
                && !novoStatus.equals("SUSPENSO")
                && !novoStatus.equals("INATIVO"))) {

            return ResponseEntity
                    .badRequest()
                    .body("Status inválido.");
        }

        return repository.findById(id).map(barbeiro -> {

            barbeiro.setStatus(novoStatus);

            repository.save(barbeiro);

            return ResponseEntity.ok()
                    .body("Status atualizado para " + novoStatus);

        }).orElse(ResponseEntity.notFound().build());
    }
}