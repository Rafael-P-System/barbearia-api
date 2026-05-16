package barbearia.repository;

import barbearia.entity.FilaEspera;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface FilaEsperaRepository extends JpaRepository<FilaEspera, Long> {

    Optional<FilaEspera> findFirstByBarbeiroIdAndDataAndStatusOrderByCriadoEmAsc(
            Long barbeiroId,
            LocalDate data,
            String status
    );
}