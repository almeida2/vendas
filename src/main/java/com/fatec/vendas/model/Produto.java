package com.fatec.vendas.model;

import jakarta.persistence.*;

@Entity
@Table(name = "produtos")
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String codigo;
    private String descricao;
    private double preco;
    private int quantidadeEstoque;

    public Produto() {
    }

    public Produto(String codigo, String descricao, double preco, int quantidadeEstoque) {
        this.codigo = codigo;
        this.descricao = descricao;
        this.preco = preco;
        this.quantidadeEstoque = quantidadeEstoque;
    }

    // decrementa o estoque se houver saldo
    public void baixarEstoque(int quantidade) {
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
        this.id = id;
    }
    public void setCodigo(String codigo) {
        if(codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("O código do produto não pode ser nulo ou vazio.");
        }   
        this.codigo = codigo;
    }
    public String getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        if(descricao == null || descricao.trim().isEmpty()) {
            throw new IllegalArgumentException("A descrição do produto não pode ser nula ou vazia.");
        }
        this.descricao = descricao;
    }

    public double getPreco() {
        return preco;
    }

        public void setPreco(double preco) {
            if(preco < 0) {
                throw new IllegalArgumentException("O preço do produto não pode ser negativo.");
            }
            this.preco = preco;
    }

    public int getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(int quantidadeEstoque) {
        if(quantidadeEstoque < 0) {
            throw new IllegalArgumentException("A quantidade em estoque não pode ser negativa.");
        }
        this.quantidadeEstoque = quantidadeEstoque;
    }
}