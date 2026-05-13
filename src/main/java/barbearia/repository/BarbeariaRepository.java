package barbearia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import barbearia.entity.Barbearia;

@Repository
public interface BarbeariaRepository extends JpaRepository<Barbearia, Long> {
    Barbearia findByEmail(String email);
}