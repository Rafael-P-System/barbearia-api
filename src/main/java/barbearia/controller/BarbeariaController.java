package barbearia.controller;

import barbearia.entity.Barbearia;
import barbearia.repository.BarbeariaRepository;
import barbearia.security.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/barbearia")
@CrossOrigin(origins = "*")
public class BarbeariaController {

    @Autowired
    private BarbeariaRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // =========================
    // 📋 LISTAR
    // =========================
    @GetMapping
    public ResponseEntity<?> listar() {
        return ResponseEntity.ok(repository.findAll());
    }

    // =========================
    // ➕ CADASTRO + LOGIN AUTOMÁTICO
    // =========================
    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastrar(@RequestBody Barbearia barbearia) {

        if (barbearia.getEmail() == null || barbearia.getSenha() == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("erro", "Email e senha obrigatórios"));
        }

        barbearia.setSenha(passwordEncoder.encode(barbearia.getSenha()));

        if (barbearia.getAberto() == null) barbearia.setAberto(true);
        if (barbearia.getAtivo() == null) barbearia.setAtivo(true);
        if (barbearia.getStatus() == null) barbearia.setStatus("ATIVO");
        if (barbearia.getNivel() == null) barbearia.setNivel("BARBEIRO");

        Barbearia salva = repository.save(barbearia);

        String token = jwtUtil.generateToken(salva.getEmail());

        return ResponseEntity.ok(Map.of(
                "mensagem", "Cadastro realizado",
                "token", token,
                "id", salva.getId()
        ));
    }

    // =========================
    // 🔐 LOGIN (CORRIGIDO)
    // =========================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Barbearia dados) {

        Barbearia barb = repository.findByEmail(dados.getEmail());

        if (barb != null) {

            if (passwordEncoder.matches(dados.getSenha(), barb.getSenha())) {

                String token = jwtUtil.generateToken(barb.getEmail());

                return ResponseEntity.ok(Map.of(
                        "token", token,
                        "id", barb.getId(),
                        "nome", barb.getNome()
                ));
            }
        }

        return ResponseEntity.status(401)
                .body(Map.of("erro", "Email ou senha inválidos"));
    }

    // =========================
    // 🔓 ABRIR
    // =========================
    @PutMapping("/{id}/abrir")
    public ResponseEntity<?> abrir(@PathVariable Long id) {

        Barbearia b = repository.findById(id).orElse(null);

        if (b == null) {
            return ResponseEntity.notFound().build();
        }

        if (!Boolean.TRUE.equals(b.getAtivo())) {
            return ResponseEntity.status(403)
                    .body(Map.of("erro", "Conta bloqueada"));
        }

        b.setAberto(true);
        repository.save(b);

        return ResponseEntity.ok(Map.of("mensagem", "Barbearia aberta"));
    }

    // =========================
    // 🔒 FECHAR
    // =========================
    @PutMapping("/{id}/fechar")
    public ResponseEntity<?> fechar(@PathVariable Long id) {

        Barbearia b = repository.findById(id).orElse(null);

        if (b == null) {
            return ResponseEntity.notFound().build();
        }

        b.setAberto(false);
        repository.save(b);

        return ResponseEntity.ok(Map.of("mensagem", "Barbearia fechada"));
    }

    // =========================
    // 📊 STATUS
    // =========================
    @GetMapping("/{id}/status")
    public ResponseEntity<?> status(@PathVariable Long id) {

        Barbearia b = repository.findById(id).orElse(null);

        if (b == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(Map.of(
                "aberto", b.getAberto(),
                "ativo", b.getAtivo(),
                "status", b.getStatus()
        ));
    }

    // =========================
    // 🚫 BLOQUEAR (SAAS)
    // =========================
    @PutMapping("/{id}/bloquear-saas")
    public ResponseEntity<?> bloquear(@PathVariable Long id) {

        Barbearia b = repository.findById(id).orElse(null);

        if (b == null) {
            return ResponseEntity.notFound().build();
        }

        b.setAtivo(false);
        b.setAberto(false);
        b.setStatus("BLOQUEADO");

        repository.save(b);

        return ResponseEntity.ok(Map.of(
                "mensagem", "Barbearia bloqueada"
        ));
    }

    // =========================
    // ✅ DESBLOQUEAR
    // =========================
    @PutMapping("/{id}/desbloquear-saas")
    public ResponseEntity<?> desbloquear(@PathVariable Long id) {

        Barbearia b = repository.findById(id).orElse(null);

        if (b == null) {
            return ResponseEntity.notFound().build();
        }

        b.setAtivo(true);
        b.setStatus("ATIVO");

        repository.save(b);

        return ResponseEntity.ok(Map.of(
                "mensagem", "Barbearia desbloqueada"
        ));
    }
}