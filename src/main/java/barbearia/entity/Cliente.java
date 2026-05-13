package barbearia.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "clientes")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = true)
    private String telefone;

    @Column(unique = true, nullable = false)
    private String email;

    @JsonIgnore // 🔐 NUNCA retorna senha
    @Column(nullable = false)
    private String senha;

    // =========================
    // RELACIONAMENTO
    // =========================
    @OneToMany(mappedBy = "cliente")
    @JsonIgnore
    private List<Agendamento> agendamentos;
}