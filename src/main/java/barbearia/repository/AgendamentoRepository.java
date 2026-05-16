package barbearia.repository;
import barbearia.entity.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    // 📅 Agenda por barbeiro
    List<Agendamento> findByBarbeiroId(Long barbeiroId);

    List<Agendamento> findByData(LocalDate data);

    List<Agendamento> findByBarbeiroIdAndData(Long barbeiroId, LocalDate data);

    List<Agendamento> findByBarbeiroIdAndDataOrderByHoraAsc(Long barbeiroId, LocalDate data);

    // 📆 Intervalo (financeiro)
    List<Agendamento> findByBarbeiroIdAndDataBetween(
            Long barbeiroId,
            LocalDate inicio,
            LocalDate fim
    );

    // 💰 Status
    List<Agendamento> findByStatus(String status);

    List<Agendamento> findByBarbeiroIdAndStatus(Long barbeiroId, String status);

    List<Agendamento> findByBarbeiroIdAndDataAndStatus(
            Long barbeiroId,
            LocalDate data,
            String status
    );

    // 📊 Dashboard financeiro completo
    List<Agendamento> findByBarbeiroIdAndDataBetweenAndStatus(
            Long barbeiroId,
            LocalDate inicio,
            LocalDate fim,
            String status
    );
}