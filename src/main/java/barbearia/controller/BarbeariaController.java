package barbearia.controller;

import barbearia.repository.BarbeariaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/barbearia")
@CrossOrigin(origins = "*")
public class BarbeariaController {

    @Autowired
    private BarbeariaRepository repository;

    @PutMapping("/{id}/abrir")
    public ResponseEntity<?> abrir(@PathVariable Long id) {
        return repository.findById(id).map(b -> {
            b.setAberto(true);
            repository.save(b);
            return ResponseEntity.ok("Aberto");
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/fechar")
    public ResponseEntity<?> fechar(@PathVariable Long id) {
        return repository.findById(id).map(b -> {
            b.setAberto(false);
            repository.save(b);
            return ResponseEntity.ok("Fechado");
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<?> status(@PathVariable Long id) {
        return repository.findById(id).map(b ->
                ResponseEntity.ok(Map.of("aberto", b.getAberto()))
        ).orElse(ResponseEntity.notFound().build());
    }
}