package barbearia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import barbearia.entity.Servico;
import java.util.Optional;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long> {
    
    // Caso você precise buscar um serviço pelo nome exato no futuro
    Optional<Servico> findByNome(String nome);
}