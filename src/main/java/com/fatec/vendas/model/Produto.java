package com.fatec.vendas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "produtos")
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Codigo do produto invalido")
    @Column(nullable = false, unique = true)
    private String codigo;

    @NotBlank(message = "Descricao do produto invalida")
    @Column(nullable = false)
    private String descricao;

    @Positive(message = "Preco do produto invalido")
    @Column(nullable = false)
    private double preco;

    // atributo para controle de estoque
    @Positive(message = "Quantidade em estoque do produto invalida")
    @Column(name = "quantidade_estoque", nullable = false)
    private int quantidadeEstoque;

    public Produto() {
    }

    public Produto(String codigo, String descricao, double preco, int quantidadeEstoque) {
        setCodigo(codigo);
        setDescricao(descricao);
        setPreco(preco);
        setQuantidadeEstoque(quantidadeEstoque);
    }

    // decrementa o estoque se houver saldo
    public void baixarEstoque(int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade para baixa deve ser maior que zero");
        }
        if (quantidade > this.quantidadeEstoque) {
            throw new IllegalArgumentException("Estoque insuficiente para o produto: " + this.descricao +
                    " (Disponível: " + this.quantidadeEstoque + ", Solicitado: " + quantidade + ")");
        }
        this.quantidadeEstoque -= quantidade;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        if (id != null && id <= 0) {
            throw new IllegalArgumentException("Id do produto invalido");
        }
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("Codigo do produto invalido");
        }
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        if (descricao == null || descricao.trim().isEmpty()) {
            throw new IllegalArgumentException("Descricao do produto invalida");
        }
        this.descricao = descricao;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        if (preco <= 0) {
            throw new IllegalArgumentException("Preco do produto invalido");
        }
        this.preco = preco;
    }

    public int getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(int quantidadeEstoque) {
        if (quantidadeEstoque <= 0) {
            throw new IllegalArgumentException("Quantidade em estoque do produto invalida");
        }
        this.quantidadeEstoque = quantidadeEstoque;
    }

    // Metodos legados para retrocompatibilidade
    @Deprecated
    public String getNome() {
        return this.descricao;
    }

    @Deprecated
    public void setNome(String nome) {
        setDescricao(nome);
    }
}