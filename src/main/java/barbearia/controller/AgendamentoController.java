package barbearia.controller;

import barbearia.entity.Agendamento;
import barbearia.entity.Barbearia;
import barbearia.entity.Cliente;
import barbearia.entity.FilaEspera;
import barbearia.repository.AgendamentoRepository;
import barbearia.repository.BarbeariaRepository;
import barbearia.repository.FilaEsperaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/agendamentos")
@CrossOrigin(origins = "*")
public class AgendamentoController {

    @Autowired
    private AgendamentoRepository repository;

    @Autowired
    private BarbeariaRepository barbeariaRepository;

    @Autowired
    private FilaEsperaRepository filaRepository;

    // =========================
    // 📋 LISTAR
    // =========================
    @GetMapping
    public List<Agendamento> listarTodos() {
        return repository.findAll();
    }

    // =========================
    // 📅 AGENDAR
    // =========================
    @PostMapping("/agendar")
    public ResponseEntity<?> agendar(@RequestBody Agendamento novo) {

        Barbearia barbeiro = barbeariaRepository
                .findById(novo.getBarbeiro().getId())
                .orElse(null);

        if (barbeiro == null) {
            return ResponseEntity.badRequest().body("Barbearia não encontrada");
        }

        if (!Boolean.TRUE.equals(barbeiro.getAberto())) {
            return ResponseEntity.badRequest().body("Barbearia fechada");
        }

        LocalTime agora = LocalTime.now();
        LocalDate hoje = LocalDate.now();

        // 🔥 REGRA: joga pro dia seguinte se já passou
        if (novo.getHora().isBefore(agora)) {
            novo.setData(hoje.plusDays(1));
        } else {
            novo.setData(hoje);
        }

        // 🔥 VERIFICA SE DIA ESTÁ CHEIO
        boolean diaCheio = horariosDisponiveis(novo.getBarbeiro().getId()).isEmpty();

        if (diaCheio) {

            FilaEspera fila = new FilaEspera();
            fila.setNomeCliente(novo.getCliente().getNome());
            fila.setData(LocalDate.now());
            fila.setBarbeiroId(novo.getBarbeiro().getId());
            fila.setCriadoEm(LocalDateTime.now());
            fila.setStatus("ESPERANDO");

            filaRepository.save(fila);

            return ResponseEntity.ok("Dia cheio. Cliente adicionado na fila.");
        }

        // 🔥 BLOQUEIO DE HORÁRIO
        boolean existe = repository
                .findByBarbeiroIdAndData(novo.getBarbeiro().getId(), novo.getData())
                .stream()
                .anyMatch(a ->
                        a.getHora().equals(novo.getHora()) &&
                        "AGENDADO".equals(a.getStatus())
                );

        if (existe) {
            return ResponseEntity.badRequest().body("Horário já ocupado");
        }

        novo.setStatus("AGENDADO");

        return ResponseEntity.ok(repository.save(novo));
    }

    // =========================
    // ❌ CANCELAR
    // =========================
    @PutMapping("/cancelar/{id}")
    public ResponseEntity<?> cancelar(@PathVariable Long id) {

        return repository.findById(id).map(a -> {

            a.setStatus("CANCELADO");

            return ResponseEntity.ok(repository.save(a));

        }).orElse(ResponseEntity.notFound().build());
    }

    // =========================
    // ✅ FINALIZAR
    // =========================
    @PutMapping("/finalizar/{id}")
    public ResponseEntity<?> finalizar(@PathVariable Long id) {

        return repository.findById(id).map(a -> {

            a.setStatus("FINALIZADO");

            return ResponseEntity.ok(repository.save(a));

        }).orElse(ResponseEntity.notFound().build());
    }

    // =========================
    // ⏱ ATRASO +20 MIN
    // =========================
    @PutMapping("/atraso/{barbeiroId}")
    public ResponseEntity<?> atraso(@PathVariable Long barbeiroId) {

        List<Agendamento> lista = repository
                .findByBarbeiroIdAndData(barbeiroId, LocalDate.now());

        lista.forEach(a -> {
            if ("AGENDADO".equals(a.getStatus())) {
                a.setHora(a.getHora().plusMinutes(20));
            }
        });

        repository.saveAll(lista);

        // 🔥 PUXA FILA
        var proximo = filaRepository
                .findFirstByBarbeiroIdAndDataAndStatusOrderByCriadoEmAsc(
                        barbeiroId,
                        LocalDate.now(),
                        "ESPERANDO"
                );

        if (proximo.isPresent()) {

            FilaEspera fila = proximo.get();

            Agendamento novo = new Agendamento();
            novo.setData(LocalDate.now());
            novo.setHora(LocalTime.of(18, 40));
            novo.setStatus("AGENDADO");

            Cliente cliente = new Cliente();
            cliente.setNome(fila.getNomeCliente());
            novo.setCliente(cliente);

            repository.save(novo);

            fila.setStatus("CHAMADO");
            filaRepository.save(fila);
        }

        return ResponseEntity.ok("Atraso aplicado + fila atualizada");
    }

    // =========================
    // ✂️ ENCAIXE MANUAL
    // =========================
    @PostMapping("/encaixe")
    public ResponseEntity<?> encaixe(@RequestBody Agendamento novo) {

        novo.setData(LocalDate.now());
        novo.setHora(LocalTime.now().plusMinutes(5));
        novo.setStatus("AGENDADO");

        return ResponseEntity.ok(repository.save(novo));
    }

    // =========================
    // 📅 HORÁRIOS DISPONÍVEIS
    // =========================
    @GetMapping("/disponiveis/{barbeiroId}")
    public List<LocalTime> horariosDisponiveis(@PathVariable Long barbeiroId) {

        List<LocalTime> todos = new ArrayList<>();

        LocalTime inicio = LocalTime.of(8, 0);
        LocalTime fim = LocalTime.of(19, 0);

        while (!inicio.isAfter(fim)) {
            todos.add(inicio);
            inicio = inicio.plusMinutes(40);
        }

        LocalDate hoje = LocalDate.now();

        List<LocalTime> ocupados = repository
                .findByBarbeiroIdAndData(barbeiroId, hoje)
                .stream()
                .filter(a -> "AGENDADO".equals(a.getStatus()))
                .map(Agendamento::getHora)
                .toList();

        return todos.stream()
                .filter(h -> !ocupados.contains(h))
                .toList();
    }
}