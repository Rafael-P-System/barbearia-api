package barbearia.entity;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "clientes")
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

    @JsonIgnore // 🔐 NUNCA retorna senha no JSON
    @Column(nullable = false)
    private String senha;

    @OneToMany(mappedBy = "cliente")
    @JsonIgnore
    private List<Agendamento> agendamentos;

    // =========================
    // CONSTRUTORES
    // =========================
    public Cliente() {}

    public Cliente(Long id, String nome, String telefone, String email, String senha) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
        this.senha = senha;
    }

    // =========================
    // GETTERS E SETTERS (Manuais para evitar erros de IDE)
    // =========================
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public List<Agendamento> getAgendamentos() { return agendamentos; }
    public void setAgendamentos(List<Agendamento> agendamentos) { this.agendamentos = agendamentos; }
}
