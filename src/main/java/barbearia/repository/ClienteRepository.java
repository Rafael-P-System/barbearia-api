package barbearia.repository; // Se esta linha estiver diferente, dá erro no Service

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import barbearia.entity.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByEmail(String email);
}