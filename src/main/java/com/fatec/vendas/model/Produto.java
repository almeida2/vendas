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

    @NotBlank(message = "Nome do produto invalido")
    @Column(nullable = false)
    private String nome;

    @Positive(message = "Preco do produto invalido")
    @Column(nullable = false)
    private double preco;

    // atributo para controle de estoque
    @Positive(message = "Quantidade em estoque do produto invalida")
    @Column(name = "quantidade_estoque", nullable = false)
    private int quantidadeEstoque;

    public Produto() {
    }

    public Produto(String nome, double preco, int quantidadeEstoque) {
        this.nome = nome;
        this.preco = preco;
        this.quantidadeEstoque = quantidadeEstoque;
    }

    // decrementa o estoque se houver saldo
    public void baixarEstoque(int quantidade) {
        if (quantidade > this.quantidadeEstoque) {
            throw new IllegalArgumentException("Estoque insuficiente para o produto: " + this.nome +
                    " (Disponível: " + this.quantidadeEstoque + ", Solicitado: " + quantidade + ")");
        }
        this.quantidadeEstoque -= quantidade;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(int quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }
}