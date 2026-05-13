package barbearia.controller;

import barbearia.entity.Barbearia;
import barbearia.repository.BarbeariaRepository;
import barbearia.security.JwtUtil;

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
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // =========================
    // 🔐 VALIDAR ADMIN
    // =========================
    private boolean isAdmin(String token) {
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                return false;
            }

            String email = jwtUtil.validateToken(token.replace("Bearer ", ""));

            Barbearia user = repository.findByEmail(email);

            return user != null &&
                   user.getNivel() != null &&
                   user.getNivel().equalsIgnoreCase("ADMIN");

        } catch (Exception e) {
            return false;
        }
    }

    // =========================
    // ✂️ CRIAR BARBEIRO (SÓ ADMIN)
    // =========================
    @PostMapping("/barbeiro")
    public ResponseEntity<?> criarBarbeiro(
            @RequestHeader("Authorization") String token,
            @RequestBody Barbearia barbeiro) {

        if (!isAdmin(token)) {
            return ResponseEntity.status(403).body("Acesso negado");
        }

        if (barbeiro.getEmail() == null || barbeiro.getSenha() == null) {
            return ResponseEntity.badRequest().body("Email e senha obrigatórios");
        }

        barbeiro.setNivel("BARBEIRO");
        barbeiro.setAtivo(true);
        barbeiro.setSenha(passwordEncoder.encode(barbeiro.getSenha()));

        return ResponseEntity.ok(repository.save(barbeiro));
    }

    // =========================
    // 📋 LISTAR BARBEIROS
    // =========================
    @GetMapping("/barbeiros")
    public ResponseEntity<?> listarBarbeiros(@RequestHeader("Authorization") String token) {

        if (!isAdmin(token)) {
            return ResponseEntity.status(403).body("Acesso negado");
        }

        List<Barbearia> lista = repository.findAll()
                .stream()
                .filter(u -> "BARBEIRO".equalsIgnoreCase(u.getNivel()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(lista);
    }

    // =========================
    // 🚫 BLOQUEAR USUÁRIO
    // =========================
    @PutMapping("/usuario/{id}/bloquear")
    public ResponseEntity<?> bloquear(@RequestHeader("Authorization") String token,
                                      @PathVariable Long id) {

        if (!isAdmin(token)) {
            return ResponseEntity.status(403).body("Acesso negado");
        }

        Barbearia user = repository.findById(id).orElse(null);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        user.setAtivo(false);
        repository.save(user);

        return ResponseEntity.ok(Map.of("mensagem", "Usuário bloqueado"));
    }

    // =========================
    // ✅ DESBLOQUEAR
    // =========================
    @PutMapping("/usuario/{id}/desbloquear")
    public ResponseEntity<?> desbloquear(@RequestHeader("Authorization") String token,
                                         @PathVariable Long id) {

        if (!isAdmin(token)) {
            return ResponseEntity.status(403).body("Acesso negado");
        }

        Barbearia user = repository.findById(id).orElse(null);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        user.setAtivo(true);
        repository.save(user);

        return ResponseEntity.ok(Map.of("mensagem", "Usuário desbloqueado"));
    }

    // =========================
    // 📊 LISTAR TODOS
    // =========================
    @GetMapping("/usuarios")
    public ResponseEntity<?> listarTodos(@RequestHeader("Authorization") String token) {

        if (!isAdmin(token)) {
            return ResponseEntity.status(403).body("Acesso negado");
        }

        return ResponseEntity.ok(repository.findAll());
    }
}