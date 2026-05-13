package barbearia.controller;

import barbearia.entity.Agendamento;
import barbearia.entity.FilaEspera;
import barbearia.repository.AgendamentoRepository;
import barbearia.repository.FilaEsperaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/agendamentos")
@CrossOrigin(origins = "*")
public class AgendamentoController {

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private FilaEsperaRepository filaRepository;

    // =========================
    // 📅 AGENDAR OU FILA
    // =========================
    @PostMapping("/agendar")
    public ResponseEntity<?> agendar(@RequestBody Agendamento agendamento) {

        // 🔥 VERIFICA MANUAL (SEM REPOSITORY CUSTOM)
        boolean ocupado = agendamentoRepository.findAll().stream()
                .anyMatch(a ->
                        a.getBarbeiro().getId().equals(agendamento.getBarbeiro().getId()) &&
                        a.getData().equals(agendamento.getData()) &&
                        a.getHora().equals(agendamento.getHora())
                );

        // 🔴 SE OCUPADO → FILA
        if (ocupado) {

            FilaEspera fila = new FilaEspera();
            fila.setCliente(agendamento.getCliente());
            fila.setBarbeiro(agendamento.getBarbeiro());
            fila.setData(agendamento.getData());
            fila.setCriadoEm(LocalDateTime.now());
            fila.setStatus("ESPERANDO");

            filaRepository.save(fila);

            return ResponseEntity.ok(
                    Map.of(
                            "tipo", "FILA",
                            "mensagem", "Horário cheio. Você entrou na fila."
                    )
            );
        }

        // 🟢 AGENDAMENTO NORMAL
        agendamento.setStatus("AGENDADO");

        Agendamento salvo = agendamentoRepository.save(agendamento);

        return ResponseEntity.ok(
                Map.of(
                        "tipo", "AGENDADO",
                        "dados", salvo
                )
        );
    }

    // =========================
    // ✅ FINALIZAR E CHAMAR PRÓXIMO
    // =========================
    @PutMapping("/{id}/finalizar")
    public ResponseEntity<?> finalizar(@PathVariable Long id) {

        return agendamentoRepository.findById(id).map(agendamento -> {

            agendamento.setStatus("FINALIZADO");
            agendamentoRepository.save(agendamento);

            // 🔥 PEGA O PRIMEIRO DA FILA
            FilaEspera proximo = filaRepository.findAll().stream()
                    .filter(f ->
                            f.getBarbeiro().getId().equals(agendamento.getBarbeiro().getId()) &&
                            f.getData().equals(agendamento.getData()) &&
                            "ESPERANDO".equals(f.getStatus())
                    )
                    .sorted((a, b) -> a.getCriadoEm().compareTo(b.getCriadoEm()))
                    .findFirst()
                    .orElse(null);

            if (proximo != null) {

                Agendamento novo = new Agendamento();
                novo.setCliente(proximo.getCliente());
                novo.setBarbeiro(proximo.getBarbeiro());
                novo.setData(agendamento.getData());
                novo.setHora(agendamento.getHora());
                novo.setStatus("AGENDADO");

                agendamentoRepository.save(novo);

                filaRepository.delete(proximo);

                return ResponseEntity.ok(
                        Map.of(
                                "mensagem", "Chamando próximo da fila",
                                "cliente", proximo.getCliente().getNome()
                        )
                );
            }

            return ResponseEntity.ok(
                    Map.of("mensagem", "Finalizado. Sem fila.")
            );

        }).orElse(ResponseEntity.notFound().build());
    }
}