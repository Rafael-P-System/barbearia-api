package barbearia.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "fila_espera")
public class FilaEspera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeCliente;

    private LocalDate data;

    private Long barbeiroId;

    private LocalDateTime criadoEm;

    private String status; // ESPERANDO, CHAMADO, CANCELADO

    // 🔥 CORREÇÃO: Adicionado o campo posição para alinhar com o Controller
    private int posicao; 

    // ==========================================
    // GETTERS E SETTERS (Manuais e seguros)
    // ==========================================
    public Long getId() { return id; }

    public String getNomeCliente() { return nomeCliente; }
    public void setNomeCliente(String nomeCliente) { this.nomeCliente = nomeCliente; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public Long getBarbeiroId() { return barbeiroId; }
    public void setBarbeiroId(Long barbeiroId) { this.barbeiroId = barbeiroId; }

    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // 🔥 CORREÇÃO: Adicionados os métodos que o Controller estava procurando
    public int getPosicao() { return posicao; }
    public void setPosicao(int posicao) { this.posicao = posicao; }
}