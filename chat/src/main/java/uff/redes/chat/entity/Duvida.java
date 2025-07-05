package uff.redes.chat.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Entity
public class Duvida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nomeAluno;

    @Column(nullable = false)
    private String turma;

    private String descricao;

    @Lob
    @Column(columnDefinition = "BLOB")
    @Basic(fetch = FetchType.LAZY)
    private byte[] dadosImagem;

    @Column(nullable = false)
    private OffsetDateTime dataCriacao;

    @PrePersist
    protected void onCreate() {
        dataCriacao = OffsetDateTime.now();
    }

    public Duvida() {
        this.dataCriacao = OffsetDateTime.now();
    }

    public Duvida(String nomeAluno, String turma, String descricao, byte[] dadosImagem) {
        this.nomeAluno = nomeAluno;
        this.turma = turma;
        this.descricao = descricao;
        this.dadosImagem = dadosImagem;
        this.dataCriacao = OffsetDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeAluno() {
        return nomeAluno;
    }

    public void setNomeAluno(String nomeAluno) {
        this.nomeAluno = nomeAluno;
    }

    public String getTurma() {
        return turma;
    }

    public void setTurma(String turma) {
        this.turma = turma;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public byte[] getDadosImagem() {
        return dadosImagem;
    }

    public void setDadosImagem(byte[] caminhoImagem) {
        this.dadosImagem = caminhoImagem;
    }

    public OffsetDateTime getDataCriacao() {
        return dataCriacao;
    }
}
