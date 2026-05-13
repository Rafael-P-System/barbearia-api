package barbearia.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "barbearias")
public class Barbearia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(unique = true, nullable = false)
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private String senha;

    // 🔥 AGORA É STRING (CORRIGIDO)
    private String status;

    // 🔥 AGORA É STRING (CORRIGIDO)
    private String nivel;

    @JsonIgnore
    private String firebaseToken;

    private Boolean aberto = false;

    // =========================
    // SAAS
    // =========================
    private Boolean ativo = true;
    private LocalDate dataVencimento;

    // =========================
    // GETTERS E SETTERS
    // =========================

    public Long getId() { return id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    // 🔥 STRING
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // 🔥 STRING
    public String getNivel() { return nivel; }
    public void setNivel(String nivel) { this.nivel = nivel; }

    public Boolean getAberto() { return aberto; }
    public void setAberto(Boolean aberto) { this.aberto = aberto; }

    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }

    public LocalDate getDataVencimento() { return dataVencimento; }
    public void setDataVencimento(LocalDate dataVencimento) { this.dataVencimento = dataVencimento; }
}