package barbearia.controller;

import barbearia.entity.Cliente;
import barbearia.repository.ClienteRepository;
import barbearia.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*")
public class ClienteController {

    @Autowired
    private ClienteRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // =========================
    // 📋 LISTAR
    // =========================
    @GetMapping
    public List<Cliente> listarTodos() {
        return repository.findAll();
    }

    // =========================
    // 🧾 CADASTRO
    // =========================
    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastrar(@RequestBody Cliente novo) {

        if (repository.findByEmail(novo.getEmail()).isPresent()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("erro", "Email já cadastrado"));
        }

        novo.setSenha(passwordEncoder.encode(novo.getSenha()));
        Cliente salvo = repository.save(novo);

        return ResponseEntity.ok(Map.of(
                "mensagem", "Cliente cadastrado com sucesso",
                "id", salvo.getId()
        ));
    }

    // =========================
    // 🔐 LOGIN (PADRÃO PRODUÇÃO)
    // =========================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Cliente dados) {

        Optional<Cliente> clienteOpt = repository.findByEmail(dados.getEmail());

        if (clienteOpt.isEmpty()) {
            return ResponseEntity.status(401)
                    .body(Map.of("erro", "Email ou senha inválidos"));
        }

        Cliente cliente = clienteOpt.get();

        if (!passwordEncoder.matches(dados.getSenha(), cliente.getSenha())) {
            return ResponseEntity.status(401)
                    .body(Map.of("erro", "Email ou senha inválidos"));
        }

        String token = jwtUtil.generateToken(cliente.getEmail());

        return ResponseEntity.ok(Map.of(
                "token", token,
                "cliente", Map.of(
                        "id", cliente.getId(),
                        "nome", cliente.getNome(),
                        "email", cliente.getEmail()
                )
        ));
    }

    // =========================
    // 🔎 BUSCAR POR ID
    // =========================
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(cliente -> ResponseEntity.ok(Map.of(
                        "id", cliente.getId(),
                        "nome", cliente.getNome(),
                        "email", cliente.getEmail()
                )))
                .orElse(ResponseEntity.notFound().build());
    }
}