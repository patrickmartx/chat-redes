package uff.redes.chat.entity;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "duvida_id", nullable = false)
    private Duvida duvida;

    private String remetente;

    private String conteudo;

    private String tipo;

    @Lob
    @Column(columnDefinition = "BLOB")
    @Basic(fetch = FetchType.LAZY)
    private byte[] dadosImagem;

    private OffsetDateTime timestamp;

    @PrePersist
    protected void onCreate() {
        if (timestamp == null) {
            timestamp = OffsetDateTime.now();
        }
    }

    public ChatMessage() {
    }

    public ChatMessage(Duvida duvida, String remetente, String conteudo, String tipo, byte[] dadosImagem) {
        this.duvida = duvida;
        this.remetente = remetente;
        this.conteudo = conteudo;
        this.timestamp = OffsetDateTime.now();
        this.tipo = tipo;
        this.dadosImagem = dadosImagem;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Duvida getDuvida() {
        return duvida;
    }

    public void setDuvida(Duvida duvida) {
        this.duvida = duvida;
    }

    public String getRemetente() {
        return remetente;
    }

    public void setRemetente(String remetente) {
        this.remetente = remetente;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public byte[] getDadosImagem() {
        return dadosImagem;
    }

    public void setDadosImagem(byte[] dadosImagem) {
        this.dadosImagem = dadosImagem;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
