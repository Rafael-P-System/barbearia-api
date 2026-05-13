package barbearia.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import barbearia.entity.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByEmail(String email);
}