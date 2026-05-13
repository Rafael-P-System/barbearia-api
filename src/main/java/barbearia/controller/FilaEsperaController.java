package barbearia.controller;

import barbearia.entity.FilaEspera;
import barbearia.repository.FilaEsperaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/fila-espera")
@CrossOrigin(origins = "*")
public class FilaEsperaController {

    @Autowired
    private FilaEsperaRepository repository;

    // =========================
    // LISTAR
    // =========================
    @GetMapping
    public ResponseEntity<?> listar() {
        return ResponseEntity.ok(repository.findAll());
    }

    // =========================
    // ADICIONAR
    // =========================
    @PostMapping
    public ResponseEntity<?> adicionar(@RequestBody FilaEspera fila) {

        if (fila.getData() == null) {
            fila.setData(LocalDate.now());
        }

        fila.setCriadoEm(LocalDateTime.now());
        fila.setStatus("ESPERANDO");

        // 🔥 CORREÇÃO IMPORTANTE: filtrar por data e status
        List<FilaEspera> lista = repository.findAll();

        int posicao = 1;

        for (FilaEspera f : lista) {

            if (f.getData() != null
                    && f.getData().equals(fila.getData())
                    && "ESPERANDO".equalsIgnoreCase(f.getStatus())) {

                posicao++;
            }
        }

        fila.setPosicao(posicao);

        FilaEspera salvo = repository.save(fila);

        return ResponseEntity.ok(Map.of(
                "mensagem", "Entrou na fila",
                "posicao", posicao,
                "dados", salvo
        ));
    }

    // =========================
    // MUDAR STATUS
    // =========================
    @PutMapping("/{id}/status")
    public ResponseEntity<?> mudarStatus(@PathVariable Long id,
                                         @RequestParam String novoStatus) {

        FilaEspera fila = repository.findById(id).orElse(null);

        if (fila == null) {
            return ResponseEntity.notFound().build();
        }

        fila.setStatus(novoStatus.toUpperCase());
        repository.save(fila);

        atualizarFila(fila.getData());

        return ResponseEntity.ok(Map.of(
                "mensagem", "Status atualizado",
                "dados", fila
        ));
    }

    // =========================
    // REMOVER
    // =========================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> remover(@PathVariable Long id) {

        FilaEspera fila = repository.findById(id).orElse(null);

        if (fila == null) {
            return ResponseEntity.notFound().build();
        }

        LocalDate data = fila.getData();

        repository.delete(fila);

        atualizarFila(data);

        return ResponseEntity.ok(Map.of(
                "mensagem", "Removido da fila"
        ));
    }

    // =========================
    // REORGANIZAR FILA
    // =========================
    private void atualizarFila(LocalDate data) {

        List<FilaEspera> lista = repository.findAll();

        int posicao = 1;

        for (FilaEspera f : lista) {

            if (f.getData() != null
                    && f.getData().equals(data)
                    && "ESPERANDO".equalsIgnoreCase(f.getStatus())) {

                f.setPosicao(posicao++);
            }
        }

        repository.saveAll(lista);
    }
}