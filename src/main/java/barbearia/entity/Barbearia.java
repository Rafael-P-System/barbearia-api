package barbearia.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "barbeiro")
public class Barbearia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;
    private String senha;
    private String status;
    private String nivel;
    private String firebaseToken;

    private Boolean aberto;

    // GETTERS / SETTERS (SEM LOMBOK PRA NÃO DAR ERRO)

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getNivel() { return nivel; }
    public void setNivel(String nivel) { this.nivel = nivel; }

    public String getFirebaseToken() { return firebaseToken; }
    public void setFirebaseToken(String firebaseToken) { this.firebaseToken = firebaseToken; }

    public Boolean getAberto() { return aberto; }
    public void setAberto(Boolean aberto) { this.aberto = aberto; }
}