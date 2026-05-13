package barbearia.repository;

import barbearia.entity.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    // Verifica se já existe agendamento para evitar duplicidade
    boolean existsByBarbeiroIdAndDataAndHoraAndStatus(Long barbeiroId, LocalDate data, LocalTime hora, String status);

    // Filtros necessários para o Controller
    List<Agendamento> findByBarbeiroIdAndDataAndStatus(Long barbeiroId, LocalDate data, String status);
    
    List<Agendamento> findByBarbeiroIdAndDataOrderByHoraAsc(Long barbeiroId, LocalDate data);
}