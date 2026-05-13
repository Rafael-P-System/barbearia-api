package barbearia.repository;

import barbearia.entity.FilaEspera;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface FilaEsperaRepository extends JpaRepository<FilaEspera, Long> {

    // 🔹 Lista fila do dia ordenada
    List<FilaEspera> findByDataOrderByCriadoEmAsc(LocalDate data);

    // 🔹 Lista só quem está esperando
    List<FilaEspera> findByDataAndStatusOrderByCriadoEmAsc(LocalDate data, String status);

    // 🔹 Pega o próximo da fila (primeiro)
    FilaEspera findFirstByBarbeiroIdAndDataAndStatusOrderByCriadoEmAsc(
            Long barbeiroId,
            LocalDate data,
            String status
    );
}