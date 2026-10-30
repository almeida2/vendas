package com.fatec.vendas.model;



import jakarta.persistence.*;

@Entity
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column (unique = true) 
    private String codigo;
    private String descricao;
    private double preco;
    private int quantidadeEstoque;

    public Produto() {
    }

    public Produto(String codigo, String descricao, double preco, int quantidadeEstoque) {
        setCodigo(codigo);
        setDescricao(descricao);
        setPreco(preco);
        setQuantidadeEstoque(quantidadeEstoque);
    }

    public void baixarEstoque(int quantidade) {
        if (quantidade <= 0 || quantidade > this.quantidadeEstoque) {
            throw new IllegalArgumentException("Quantidade para baixa de estoque inválida.");
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

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("O código do produto não pode ser nulo ou vazio.");
        }
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        if (descricao == null || descricao.trim().isEmpty()) {
            throw new IllegalArgumentException("A descrição do produto não pode ser nula ou vazia.");
        }
        this.descricao = descricao;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        if (preco <= 0 || Double.isNaN(preco) || Double.isInfinite(preco)) {
            throw new IllegalArgumentException("Preço do produto inválido.");
        }
        this.preco = preco;
    }

    public int getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(int quantidadeEstoque) {
        if (quantidadeEstoque <= 0) {
            throw new IllegalArgumentException("Quantidade em estoque inválida.");
        }
        this.quantidadeEstoque = quantidadeEstoque;
    }
}